/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.rs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Resolves resource methods from runtime context.
 * 
 * @author Heiko.Braun@jboss.com
 * @version $Revision$
 */
public class ResourceResolver
{
   private RuntimeContext context;

   public static ResourceResolver newInstance(RuntimeContext context)
   {
      assert context!=null;      
      return new ResourceResolver(context);
   }

   private ResourceResolver(RuntimeContext context)
   {
      this.context = context;
   }

   public ResourceMethod resolve()
     throws NoResourceException, NoMethodException
   {
      ResourceMethod resourceMethod = null;

      // Filter the set of resource classes by rejecting those whose
      // regular expression does not match uri
      List<ResourceMatch> includedResources = new ArrayList<ResourceMatch>();
      Iterator<ResourceModel> it1 = context.getRootResources().iterator();
      while(it1.hasNext())
      {
         ResourceModel model = it1.next();
         RegexQualifier qualifier = model.resolve(context.getUri());
         if(qualifier!=null)
            includedResources.add( new ResourceMatch(model, qualifier) );
      }

      if(includedResources.isEmpty())
         throw new NoResourceException("No resource matches URI '"+context.getUri()+"'");

      Collections.sort(includedResources);

      // DFS by locator, should result in a resource match
      Iterator<ResourceMatch> it2 = includedResources.iterator();
      while(it2.hasNext() && null==resourceMethod)
      {
         ResourceMatch rootResource = it2.next();
         resourceMethod = dfsResourceMatch(rootResource);
      }

      if(null == resourceMethod)
         throw new NoMethodException("No method for URI '"+context.getUri());

      // gotcha
      return resourceMethod;
   }

   /**
    * Recursive scan for resource methods.
    * Inspect a resource match for methods and it fails try the locators.
    *
    * @param dfsEntry
    * @return
    */
   private ResourceMethod dfsResourceMatch(ResourceMatch dfsEntry)
   {
      ResourceMethod resourceMethod = null;
      String nextUriToken = dfsEntry.qualifier.nextUriToken;

      // resource and subresource methods first
      resourceMethod = resolveResourceMethod(dfsEntry, nextUriToken);

      // root didn't match, so recurse locators to find a resource
      if(null == resourceMethod)
      {
         ResourceMatch subResource = resolveByLocator(dfsEntry);
         if(subResource!=null)
            resourceMethod = dfsResourceMatch(subResource);
      }

      return resourceMethod;
   }

   private ResourceMatch resolveByLocator(ResourceMatch<ResourceModel> resourceMatch)
   {
      ResourceMatch match = null;

      List<ResourceMatch> weightedResults = new ArrayList<ResourceMatch>();
      Iterator<ResourceLocator> locators = resourceMatch.model.getResourceLocator().iterator();
      while(locators.hasNext())
      {
         ResourceLocator bridge = locators.next();
         RegexQualifier qualifier = bridge.resolve(resourceMatch.qualifier.nextUriToken);
         if(qualifier!=null)
            weightedResults.add( new ResourceMatch( bridge.field(), qualifier) );
      }

      if(!weightedResults.isEmpty())
      {
         Collections.sort(weightedResults);
         match = weightedResults.get(0);
      }

      return match;
   }

   private ResourceMethod resolveResourceMethod(ResourceMatch<ResourceModel> methodTarget, String uriToken)
   {
      ResourceMethod match = null;
      List<ResourceMatch<ResourceMethod>> matches = new ArrayList<ResourceMatch<ResourceMethod>>();

      if("".equals(uriToken) || "/".equals(uriToken)) // resources methods
      {
         // use any available resource method for further mathing by mimetype, etc
         for(ResourceMethod resourceMethod : methodTarget.model.getResourceMethods())
         {
            matches.add(
              new ResourceMatch<ResourceMethod>(resourceMethod, RegexQualifier.NONE)
            );
         }
      }
      else // subresource methods
      {
         List<ResourceMethod> methods = methodTarget.model.getSubResourceMethods();
         Iterator<ResourceMethod> it = methods.iterator();
         while(it.hasNext())
         {
            ResourceMethod method = it.next();
            RegexQualifier qualifier = method.resolve(uriToken);
            if(qualifier!=null && ("".equals(qualifier.nextUriToken) || "/".equals(qualifier.nextUriToken)))
            {
               matches.add(
                 new ResourceMatch<ResourceMethod>(method, qualifier)
               );
            }
         }
      }
      
      if(!matches.isEmpty())
      {
         Collections.sort(matches);
         match = contentNegotiation(matches);
      }

      return match;
   }

   // TODO: error handling and media-type matching
   private ResourceMethod contentNegotiation(List<ResourceMatch<ResourceMethod>> matches)
   {
      ResourceMethod match = null;
      for(ResourceMatch<ResourceMethod> candiate : matches)
      {
         // Match by request method -> HTTP 405
         if(context.getRequestMethod() == candiate.model.getMethodHTTP())
         {
            match = candiate.model;
            break;
         }

         // Match by supported input data format -> HTTP 415
         
         // Match by supported output data format -> HTTP 406
         
      }
      return match;
   }

   class ResourceMatch<T> implements Comparable
   {
      final T model;
      final RegexQualifier qualifier;

      public ResourceMatch(T model, RegexQualifier weight)
      {
         this.model = model;
         this.qualifier = weight;
      }

      public int compareTo(Object o)
      {
         return qualifier.compareTo(((ResourceMatch<T>)o).qualifier);
      }

      public String toString()
      {
         return "ResourceMatch {model="+model+", weight="+ qualifier +"}";
      }
   }

}

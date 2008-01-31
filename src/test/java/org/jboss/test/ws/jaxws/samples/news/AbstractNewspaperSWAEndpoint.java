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
package org.jboss.test.ws.jaxws.samples.news;

import java.util.Date;

import javax.activation.DataHandler;

import org.jboss.logging.Logger;


public class AbstractNewspaperSWAEndpoint
{
   private Logger log = Logger.getLogger(this.getClass());
   
   public EditionSWA getNewspaperEdition(String newspaperId)
   {
      log.info("Newspaper edition requested: " + newspaperId);
      EditionSWA edition = new EditionSWA();
      edition.setContent(new DataHandler("This is the newspaper document with id " + newspaperId, "text/plain"));
      edition.setDate(new Date());
      edition.setId(newspaperId);
      return edition;
   }
   
   public String[] getNewspaperEditionIdList(Date from, Date to)
   {
      String[] ids = new String[2];
      ids[0] = "doc01";
      ids[1] = "doc02";
      return ids;
   }
}

/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *     Samuel Padgett       - initial API and implementation
 *******************************************************************************/
package org.eclipse.lyo.oslc.v3.sample.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.jena.riot.Lang;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.ResourceUtils;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.lyo.oslc.v3.sample.MediaTypeContants.APPLICATION_JSON_LD;
import static org.eclipse.lyo.oslc.v3.sample.MediaTypeContants.TEXT_TURTLE;

@Provider
@Consumes({ TEXT_TURTLE, APPLICATION_JSON_LD, APPLICATION_JSON })
public class ModelMessageBodyReader implements MessageBodyReader<Model> {
	private final static String FAKE_BASE =
			"http://eclipse.org/lyo/" + ModelMessageBodyReader.class.getPackage() + "/";

	@Override
	public boolean isReadable(Class<?> type,
							  Type genericType,
							  Annotation[] annotations,
							  MediaType mediaType) {
		return true;
	}

	private void keepRelative(ResIterator i) {
		while (i.hasNext()) {
			keepRelative(i.next());
		}
	}

	private void keepRelative(NodeIterator i) {
		while (i.hasNext()) {
			keepRelative(i.next());
		}
	}

	private void keepRelative(RDFNode n) {
		if (n.isURIResource()) {
			Resource r = (Resource) n;
			String uri = r.getURI();
			if (uri.startsWith(FAKE_BASE)) {
				String relativeURI = uri.substring(FAKE_BASE.length());
				ResourceUtils.renameResource(r, relativeURI);
			}
		}
	}

	@Override
	public Model readFrom(Class<Model> type,
						  Type genericType,
						  Annotation[] annotations,
						  MediaType mediaType,
						  MultivaluedMap<String, String> httpHeaders,
						  InputStream entityStream) throws IOException,
			WebApplicationException {
		final Lang lang;
		if (mediaType.isCompatible(MediaType.valueOf("text/turtle"))) {
			lang = Lang.TURTLE;
		} else {
			lang = Lang.JSONLD;
		}

		Model m = ModelFactory.createDefaultModel();

		// Take care to preserve relative URIs.
		m.read(entityStream, FAKE_BASE, lang.getName());
		keepRelative(m.listSubjects());
		keepRelative(m.listObjects());

		return m;
	}
}

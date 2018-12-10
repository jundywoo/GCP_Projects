package com.ken.gcp.quiz.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class StaticFileService {

	private static final Log LOG = LogFactory.getLog(StaticFileService.class);

	public String readFileToEnd(final File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			return readFileToEnd(new FileInputStream(file), file.length());
		} catch (final FileNotFoundException e) {
			LOG.warn(e.getMessage(), e);
			return null;
		}
	}

	public String readFileToEnd(final Resource resource) {
		if (resource == null) {
			return null;
		}
		try {
			return readFileToEnd(resource.getInputStream(), 0);
		} catch (final IOException e) {
			LOG.warn(e.getMessage(), e);
			return null;
		}
	}

	public String readFileToEnd(final InputStream inputStream, final long length) {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) length);
		try {
			final byte[] buffer = new byte['?'];
			int count = 0;
			while ((count = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, count);
			}
			return new String(outputStream.toByteArray());
		} catch (final IOException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}

	public String hostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			return "Unknown Host";
		}
	}

}

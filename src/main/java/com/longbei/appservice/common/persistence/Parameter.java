/**
 * Copyright &copy; 2014-2015 <a href="https://www.3chunhui.com/webservice">webservice</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.longbei.appservice.common.persistence;

import java.util.HashMap;

public class Parameter extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Parameter(Object... values) {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				put("p" + (i + 1), values[i]);
			}
		}
	}

	public Parameter(Object[][] parameters) {
		if (parameters != null) {
			for (Object[] os : parameters) {
				if (os.length == 2) {
					put((String) os[0], os[1]);
				}
			}
		}
	}
}

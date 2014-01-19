/**
 * bFundamentals 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.thecodingbadgers.bFundamentals.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import uk.codingbadgers.bFundamentals.ConfigManager;
import uk.codingbadgers.bFundamentals.bFundamentals;

public class TestConfigManager implements InvocationHandler {
	private static interface MethodHandler {
        Object handle(TestConfigManager server, Object[] args);
    }
	
    private static final HashMap<Method, MethodHandler> methods = new HashMap<Method, MethodHandler>();
    static {
        try {
            methods.put(
                    ConfigManager.class.getMethod("getCrashPassword"),
                    new MethodHandler() {
                        public Object handle(TestConfigManager server, Object[] args) {
                            return "Password";
                        }
                    }
                );
            methods.put(
                    ConfigManager.class.getMethod("isDebugEnabled"),
                    new MethodHandler() {
                        public Object handle(TestConfigManager server, Object[] args) {
                            return true;
                        }
                    }
                );
            bFundamentals.setConfigManager(Proxy.getProxyClass(ConfigManager.class.getClassLoader(), ConfigManager.class).asSubclass(ConfigManager.class).getConstructor(InvocationHandler.class).newInstance(new TestConfigManager()));
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void setup() {}

    private TestConfigManager() {};

    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, args);
        }
        throw new UnsupportedOperationException(String.valueOf(method));
    }
}

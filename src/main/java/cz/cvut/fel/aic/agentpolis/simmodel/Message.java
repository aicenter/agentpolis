/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel;

/**
 *
 * @author fido
 */
public class Message {
	private final Object content;
	
	private final Enum type;

	public Object getContent() {
		return content;
	}

	public Enum getType() {
		return type;
	}
	
	

	public Message(Enum type, Object content) {
		this.content = content;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Message{" +
				"type=" + type +
				", content=" + content +
				'}';
	}
}

/*
* Copyright 2007 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package gldapo.entry;
import gldapo.*;
import gldapo.exception.*;
import gldapo.schema.annotation.GldapoNamingAttribute;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapRdn;

/**
 * The API reference for schema classes.
 * 
 * Schema classes do <strong>not</strong> extend this class
 * (i.e. {@code schemaObject instanceof GldapoEntry} will return false.)
 */
public abstract class GldapoEntry {
    
    /**
     * The connection to an actual LDAP directory.
     * 
     * @return The directory for this object, or {@code null} if not set yet.
     */
    public GldapoDirectory getDirectory() { return null; }
    
    /**
     * Before an <em>operation</em> (create, update, delete etc.) can be performed on an object,
     * it must have {@link GldapoDirectory directory} to perform the operation against.
     * 
     * @param directory If not an instance of {@link GldapoDirectory}, will be {@code toString()}'d
     *        and treated as a directory name and the corresponding directory will be pulled
     *        from the class's gldapo instance.
     * @throws GldapoDirectoryNotFoundException If {@code directory} is used as a name, and no directory
     *         is registered with that name.
     */
    public void setDirectory(Object directory) throws GldapoDirectoryNotFoundException {}
    
    /**
     * The <em>Base Relative Distinguished Name</em> for this entry.
     * 
     * The 'brdn' denotes the location of the entry in the directory, relative to the {@link GldapoDirectory#getBase() base}
     * of the object's directory. It is a combination of the {@link #getParent() parent}, and 
     * {@link #getNamingAttribute() namingAttribute}/{@link #getNamingValue() namingValue} properties.
     * 
     * @return The brdn, or null if not set yet
     * @throws GldapoException if the {@link #getNamingValue() naming value} for this object is {@code null}
     */
    public DistinguishedName getBrdn() throws GldapoException { return null; }
    
    /**
     * Set the location of this entry, with a dn relative to the entry's directory base.
     * 
     * The {@code brdn} is converted to a string, and then a {@link DistinguishedName}. 
     * The leading (leftmost) {@link LdapRdn rdn} is seperated from the dn and the value
     * passed to {@link #setNamingValue(String)}. Any remaining rdns are passed as a single value
     * to {@link #setParent(Object)}.
     * 
     * @param brdn An object whose string representation will be the brdn
     * @throws GldapoException if the leading rdn of {@code brdn} has a different attribute name
     *         to the defined {@link #getNamingAttribute() naming attribute} for this class
     */
    public void setBrdn(Object brdn) throws GldapoException {}
    
    /**
     * The absolute location of this entry in it's directory.
     * 
     * The {@link #getBrdn() brdn}, plus the {@link GldapoDirectory#getBase() base} 
     * of this object's directory.
     * 
     * @throws GldapoException if this object has no directory, or naming value set 
     */
    public DistinguishedName getDn() throws GldapoException { return null; }
    
    /**
     * The dn for the entry in the directory that contains this entry.
     * 
     * A dn of {@code ""} indicates that the entry's parent is the 
     * base of it's directory. If a parent has not been explicitly set,
     * an empty dn will be returned.
     * 
     * @return A dn, never {@code null}
     */
    public DistinguishedName getParent() { return null; }
    
    /**
     * Defines the location for this entry by defining it's parent container.
     * 
     * @param parent the string representation of this object will form the parent {@link DistinguishedName dn}
     */
    public void setParent(Object parent) {} 

    /**
     * The value for the {@link #getNamingAttribute() naming attribute}.
     * 
     * This defines part of the location/{@link #getBrdn() brdn} of this entry.
     * 
     * @return the naming value, or {@code null} if not yet set
     */
    public String getNamingValue() { return null; }
    
    /**
     * Sets the value of the {@link #getNamingAttribute() naming attribute}.
     * 
     * This value cannot be changed once set. See {@link #move(String,Object)} if you need
     * to move an entry (i.e. change it's naming value)
     * 
     * @param value the naming value
     * @throws GldapoException if this object already has a naming value
     */
    public void setNamingValue(String value) throws GldapoException {} 
    
    /**
     * The <em>name</em> of the property that is used to define this entry's name.
     * 
     * The naming attribute is defined by annotating a property with {@link GldapoNamingAttribute @GldapoNamingAttribute}
     * and is mandatory for schema classes.
     * 
     * @return the attribute name, never {@code null}
     */
    public String getNamingAttribute() { return null; }
    
    /**
     * Fetches the entry at {@code dn} from {@code directory}.
     * 
     * If {@code directory} is {@code null}, the {@link GldapoDirectoryRegistry#getDefaultDirectory() default directory} will
     * be used.
     * 
     * @param dn an object's whose string representation is the dn of the target entry
     * @param directory if not a {@link GldapoDirectory directory}, an objects whose string representation
     *        is the name of a registered directory
     * @return an entry object, or {@code null} if there is no entry at {@code dn}
     * @throws GldapoException if dn or directory are invalid, or an LDAP error occurs.
     * 
     */
    public static GldapoEntry getByDn(Object dn, Object directory) throws GldapoException { return null; }
    
    /**
     * Fetches the entry at {@code dn} from the the {@link GldapoDirectoryRegistry#getDefaultDirectory() default directory}.
     * 
     * Calls {@link #getByDn(Object,Object) getByDn(dn,null)}.
     */
    public static GldapoEntry getByDn(Object dn) throws GldapoException { return null; }    
    
    /**
     * Writes the object in it's entirety to the directory.
     * 
     * Before an object can be written, it must have a location and a {@link #setDirectory(Object) directory}. To have
     * a location, it must at least have a naming value.
     * 
     * @throws GldapoException If the object is in an invalid state for creating, or an LDAP error occurs.
     */
    public void create() throws GldapoException {} 
    
    /**
     * Saves any modifications made to this object to the directory.
     * 
     * This will only send any changes made, so is not suitable for {@link #create() creating} new objects.
     * If this object has no modifications, this is a no-op.
     * 
     * @throws GldapoException if the object is not in a suitable state for updating, or an LDAP error occurs.
     */
    public void update() throws GldapoException {} 
    
    /**
     * {@link #create() Creates} of {@link #update() updates} the object depending on whether it is a new object or not.
     * 
     * @throws GldapoException if the object is not in a suitable state for saving, or an LDAP error occurs.
     */
    public void save() throws GldapoException {} 
    
    /**
     * Relocate this object in the directory, after {@link #update() sending any updates}.
     * 
     * @param brdn the new {@link getBrdn() brdn} of the object (the string representation will be used).
     * @throws GldapoException if {@code brdn} is invalid, this is not an existing entry, or an LDAP error occurs.
     */
    public void move(Object brdn) throws GldapoException {} 
    
    /**
     * Relocate this object in the directory, after {@link #update() sending any updates}.
     * 
     * @param namingValue the new naming value (the string representation will be used). If {@code null},
     *        the existing naming value will be used.
     * @param parent the new parent container (the string representation will be used). If {@code null},
     *        the existing parent value will be used. Use {@code ""} to specify no parent.
     * @throws GldapoException if {@code brdn} is invalid, this is not an existing entry, or an LDAP error occurs.
     */
    public void move(Object namingValue, Object parent) throws GldapoException {} 
    
    /**
     * Replaces the entry @ {@code brdn} with this object.
     * 
     * @param brdn the location of the entry to replace (the string representation will be used).
     * @throws GldapoException if this object has no directory, or and LDAP error occurs.
     */
    public void replace(Object brdn) throws GldapoException {}
    
    /**
     * Replaces the entry at the location specified by {@code namingValue} and {@code parent} with this
     * object.
     * 
     * @param namingValue the replacee's naming value (the string representation will be used). If {@code null},
     *        this object's naming value will be used.
     * @param parent the replacee's parent container (the string representation will be used). If {@code null},
     *        this object's parent value will be used. Use {@code ""} to specify no parent.
     * @throws GldapoException if this object has no directory, or and LDAP error occurs.
     */
    public void replace(Object namingValue, Object parent) throws GldapoException {}
    
    /**
     * Replaces the entry specified by this objects's {@link #getBrdn() brdn} with this object.
     * 
     * @throws GldapoException if this object has no directory, no valid brdn, or and LDAP error occurs.
     */
    public void replace() throws GldapoException {} 
    
    /**
     * Removes the entry specified by this object's {@link #getBrdn() brdn} from the directory.
     * 
     * This <strong>will</strong> fail if the entry has children, use {@link #deleteRecursively()} in that case.
     * 
     * @throws GldapoException if this object has no directory, no valid brdn, or and LDAP error occurs.
     */
    public void delete() throws GldapoException {}
    
    /**
     * Removes the entry specified by this object's {@link #getBrdn() brdn} from the directory, and all it's children.
     * 
     * @throws GldapoException if this object has no directory, no valid brdn, or and LDAP error occurs.
     */
    public void deleteRecursively() throws GldapoException {}
    
    /**
     * Test if {@code password} is this entry's password.
     * 
     * @throws GldapoException if this object has no location or directory defined, or an LDAP error occurs
     */
    public boolean authenticate(String password) throws GldapoException 
    
}
begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
import|;
end_import

begin_comment
comment|/**  * A collection of resources accessible through a REST API.  *<p>  * To build a collection declare a resource, the map in a module, and the  * collection itself accepting the map:  *  *<pre>  * public class MyResource implements RestResource {  *   public static final TypeLiteral&lt;RestView&lt;MyResource&gt;&gt; MY_KIND =  *       new TypeLiteral&lt;RestView&lt;MyResource&gt;&gt;() {};  * }  *  * public class MyModule extends AbstractModule {  *&#064;Override  *   protected void configure() {  *     DynamicMap.mapOf(binder(), MyResource.MY_KIND);  *  *     get(MyResource.MY_KIND,&quot;action&quot;).to(MyAction.class);  *   }  * }  *  * public class MyCollection extends RestCollection&lt;TopLevelResource, MyResource&gt; {  *   private final DynamicMap&lt;RestView&lt;MyResource&gt;&gt; views;  *  *&#064;Inject  *   MyCollection(DynamicMap&lt;RestView&lt;MyResource&gt;&gt; views) {  *     this.views = views;  *   }  *  *   public DynamicMap&lt;RestView&lt;MyResource&gt;&gt; views() {  *     return views;  *   }  * }  *</pre>  *  *<p>  * To build a nested collection, implement {@link ChildCollection}.  *  * @param<P> type of the parent resource. For a top level collection this  *        should always be {@link TopLevelResource}.  * @param<R> type of resource operated on by each view.  */
end_comment

begin_interface
DECL|interface|RestCollection
specifier|public
interface|interface
name|RestCollection
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|,
name|R
extends|extends
name|RestResource
parameter_list|>
block|{
comment|/**    * Create a view to list the contents of the collection.    *<p>    * The returned view should accept the parent type to scope the search, and    * may want to take a "q" parameter option to narrow the results.    *    * @return view to list the collection.    * @throws ResourceNotFoundException if the collection cannot be listed.    * @throws AuthException if the collection requires authentication.    */
DECL|method|list ()
name|RestView
argument_list|<
name|P
argument_list|>
name|list
parameter_list|()
throws|throws
name|ResourceNotFoundException
throws|,
name|AuthException
function_decl|;
comment|/**    * Parse a path component into a resource handle.    *    * @param parent the handle to the collection.    * @param id string identifier supplied by the client. In a URL such as    *        {@code /changes/1234/abandon} this string is {@code "1234"}.    * @return a resource handle for the identified object.    * @throws ResourceNotFoundException the object does not exist, or the caller    *         is not permitted to know if the resource exists.    * @throws Exception if the implementation had any errors converting to a    *         resource handle. This results in an HTTP 500 Internal Server Error.    */
DECL|method|parse (P parent, String id)
name|R
name|parse
parameter_list|(
name|P
name|parent
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|Exception
function_decl|;
comment|/**    * Get the views that support this collection.    *<p>    * Within a resource the views are accessed as {@code RESOURCE/plugin~view}.    *    * @return map of views.    */
DECL|method|views ()
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|views
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


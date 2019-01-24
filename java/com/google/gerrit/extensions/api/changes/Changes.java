begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|changes
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
name|client
operator|.
name|ListChangesOption
import|;
end_import

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
name|common
operator|.
name|ChangeInfo
import|;
end_import

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
name|common
operator|.
name|ChangeInput
import|;
end_import

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
name|restapi
operator|.
name|NotImplementedException
import|;
end_import

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
name|restapi
operator|.
name|RestApiException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_interface
DECL|interface|Changes
specifier|public
interface|interface
name|Changes
block|{
comment|/**    * Look up a change by numeric ID.    *    *<p><strong>Note:</strong> This method eagerly reads the change. Methods that mutate the change    * do not necessarily re-read the change. Therefore, calling a getter method on an instance after    * calling a mutation method on that same instance is not guaranteed to reflect the mutation. It    * is not recommended to store references to {@code ChangeApi} instances.    *    * @param id change number.    * @return API for accessing the change.    * @throws RestApiException if an error occurred.    */
DECL|method|id (int id)
name|ChangeApi
name|id
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Look up a change by string ID.    *    * @see #id(int)    * @param id any identifier supported by the REST API, including change number, Change-Id, or    *     project~branch~Change-Id triplet.    * @return API for accessing the change.    * @throws RestApiException if an error occurred.    */
DECL|method|id (String id)
name|ChangeApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Look up a change by project, branch, and change ID.    *    * @see #id(int)    */
DECL|method|id (String project, String branch, String id)
name|ChangeApi
name|id
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Look up a change by project and numeric ID.    *    * @param project project name.    * @param id change number.    * @see #id(int)    */
DECL|method|id (String project, int id)
name|ChangeApi
name|id
parameter_list|(
name|String
name|project
parameter_list|,
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|create (ChangeInput in)
name|ChangeApi
name|create
parameter_list|(
name|ChangeInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|query ()
name|QueryRequest
name|query
parameter_list|()
function_decl|;
DECL|method|query (String query)
name|QueryRequest
name|query
parameter_list|(
name|String
name|query
parameter_list|)
function_decl|;
DECL|class|QueryRequest
specifier|abstract
class|class
name|QueryRequest
block|{
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|isNoLimit
specifier|private
name|boolean
name|isNoLimit
decl_stmt|;
DECL|field|options
specifier|private
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|withQuery (String query)
specifier|public
name|QueryRequest
name|withQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withLimit (int limit)
specifier|public
name|QueryRequest
name|withLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withNoLimit ()
specifier|public
name|QueryRequest
name|withNoLimit
parameter_list|()
block|{
name|this
operator|.
name|isNoLimit
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withStart (int start)
specifier|public
name|QueryRequest
name|withStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Set an option on the request, appending to existing options. */
DECL|method|withOption (ListChangesOption options)
specifier|public
name|QueryRequest
name|withOption
parameter_list|(
name|ListChangesOption
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|add
argument_list|(
name|options
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Set options on the request, appending to existing options. */
DECL|method|withOptions (ListChangesOption... options)
specifier|public
name|QueryRequest
name|withOptions
parameter_list|(
name|ListChangesOption
modifier|...
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Set options on the request, replacing existing options. */
DECL|method|withOptions (EnumSet<ListChangesOption> options)
specifier|public
name|QueryRequest
name|withOptions
parameter_list|(
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
DECL|method|getNoLimit ()
specifier|public
name|boolean
name|getNoLimit
parameter_list|()
block|{
return|return
name|isNoLimit
return|;
block|}
DECL|method|getStart ()
specifier|public
name|int
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
DECL|method|getOptions ()
specifier|public
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
operator|.
name|append
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|limit
operator|!=
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", limit="
argument_list|)
operator|.
name|append
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|start
operator|!=
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", start="
argument_list|)
operator|.
name|append
argument_list|(
name|start
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"options="
argument_list|)
operator|.
name|append
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNoLimit
operator|==
literal|true
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" --no-limit"
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|Changes
block|{
annotation|@
name|Override
DECL|method|id (int id)
specifier|public
name|ChangeApi
name|id
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|id (String triplet)
specifier|public
name|ChangeApi
name|id
parameter_list|(
name|String
name|triplet
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|id (String project, String branch, String id)
specifier|public
name|ChangeApi
name|id
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|id (String project, int id)
specifier|public
name|ChangeApi
name|id
parameter_list|(
name|String
name|project
parameter_list|,
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|create (ChangeInput in)
specifier|public
name|ChangeApi
name|create
parameter_list|(
name|ChangeInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|query ()
specifier|public
name|QueryRequest
name|query
parameter_list|()
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|query (String query)
specifier|public
name|QueryRequest
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit


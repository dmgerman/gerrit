begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.groups
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
name|groups
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
name|ListGroupsOption
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
name|GroupInfo
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
name|ArrayList
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
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_interface
DECL|interface|Groups
specifier|public
interface|interface
name|Groups
block|{
comment|/**    * Look up a group by ID.    *<p>    *<strong>Note:</strong> This method eagerly reads the group. Methods that    * mutate the group do not necessarily re-read the group. Therefore, calling a    * getter method on an instance after calling a mutation method on that same    * instance is not guaranteed to reflect the mutation. It is not recommended    * to store references to {@code groupApi} instances.    *    * @param id any identifier supported by the REST API, including group name or    *     UUID.    * @return API for accessing the group.    * @throws RestApiException if an error occurred.    */
DECL|method|id (String id)
name|GroupApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Create a new group with the given name and default options. */
DECL|method|create (String name)
name|GroupApi
name|create
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Create a new group. */
DECL|method|create (GroupInput input)
name|GroupApi
name|create
parameter_list|(
name|GroupInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** @return new request for listing groups. */
DECL|method|list ()
name|ListRequest
name|list
parameter_list|()
function_decl|;
DECL|class|ListRequest
specifier|abstract
class|class
name|ListRequest
block|{
DECL|field|options
specifier|private
specifier|final
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListGroupsOption
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|projects
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|projects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|groups
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|visibleToAll
specifier|private
name|boolean
name|visibleToAll
decl_stmt|;
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
DECL|field|owned
specifier|private
name|boolean
name|owned
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
DECL|field|substring
specifier|private
name|String
name|substring
decl_stmt|;
DECL|field|suggest
specifier|private
name|String
name|suggest
decl_stmt|;
DECL|method|get ()
specifier|public
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
name|map
init|=
name|getAsMap
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
name|e
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// ListGroups "helpfully" nulls out names when converting to a map.
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|name
operator|=
name|e
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|getAsMap ()
specifier|public
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
name|getAsMap
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|addOption (ListGroupsOption option)
specifier|public
name|ListRequest
name|addOption
parameter_list|(
name|ListGroupsOption
name|option
parameter_list|)
block|{
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addOptions (ListGroupsOption... options)
specifier|public
name|ListRequest
name|addOptions
parameter_list|(
name|ListGroupsOption
modifier|...
name|options
parameter_list|)
block|{
return|return
name|addOptions
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addOptions (Iterable<ListGroupsOption> options)
specifier|public
name|ListRequest
name|addOptions
parameter_list|(
name|Iterable
argument_list|<
name|ListGroupsOption
argument_list|>
name|options
parameter_list|)
block|{
for|for
control|(
name|ListGroupsOption
name|option
range|:
name|options
control|)
block|{
name|this
operator|.
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|withProject (String project)
specifier|public
name|ListRequest
name|withProject
parameter_list|(
name|String
name|project
parameter_list|)
block|{
name|projects
operator|.
name|add
argument_list|(
name|project
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addGroup (String uuid)
specifier|public
name|ListRequest
name|addGroup
parameter_list|(
name|String
name|uuid
parameter_list|)
block|{
name|groups
operator|.
name|add
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withVisibleToAll (boolean visible)
specifier|public
name|ListRequest
name|withVisibleToAll
parameter_list|(
name|boolean
name|visible
parameter_list|)
block|{
name|visibleToAll
operator|=
name|visible
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withUser (String user)
specifier|public
name|ListRequest
name|withUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withOwned (boolean owned)
specifier|public
name|ListRequest
name|withOwned
parameter_list|(
name|boolean
name|owned
parameter_list|)
block|{
name|this
operator|.
name|owned
operator|=
name|owned
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withLimit (int limit)
specifier|public
name|ListRequest
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
DECL|method|withStart (int start)
specifier|public
name|ListRequest
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
DECL|method|withSubstring (String substring)
specifier|public
name|ListRequest
name|withSubstring
parameter_list|(
name|String
name|substring
parameter_list|)
block|{
name|this
operator|.
name|substring
operator|=
name|substring
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withSuggest (String suggest)
specifier|public
name|ListRequest
name|withSuggest
parameter_list|(
name|String
name|suggest
parameter_list|)
block|{
name|this
operator|.
name|suggest
operator|=
name|suggest
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getOptions ()
specifier|public
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
DECL|method|getProjects ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getProjects
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|projects
argument_list|)
return|;
block|}
DECL|method|getGroups ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getGroups
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|groups
argument_list|)
return|;
block|}
DECL|method|getVisibleToAll ()
specifier|public
name|boolean
name|getVisibleToAll
parameter_list|()
block|{
return|return
name|visibleToAll
return|;
block|}
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getOwned ()
specifier|public
name|boolean
name|getOwned
parameter_list|()
block|{
return|return
name|owned
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
DECL|method|getSubstring ()
specifier|public
name|String
name|getSubstring
parameter_list|()
block|{
return|return
name|substring
return|;
block|}
DECL|method|getSuggest ()
specifier|public
name|String
name|getSuggest
parameter_list|()
block|{
return|return
name|suggest
return|;
block|}
block|}
comment|/**    * A default implementation which allows source compatibility    * when adding new methods to the interface.    **/
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|Groups
block|{
annotation|@
name|Override
DECL|method|id (String id)
specifier|public
name|GroupApi
name|id
parameter_list|(
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
DECL|method|create (String name)
specifier|public
name|GroupApi
name|create
parameter_list|(
name|String
name|name
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
DECL|method|create (GroupInput input)
specifier|public
name|GroupApi
name|create
parameter_list|(
name|GroupInput
name|input
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
DECL|method|list ()
specifier|public
name|ListRequest
name|list
parameter_list|()
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


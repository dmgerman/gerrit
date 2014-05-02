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
DECL|method|id (String triplet)
name|ChangeApi
name|id
parameter_list|(
name|String
name|triplet
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
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
comment|/**    * Shorthand for {@link #query(QueryParameter)} without any conditions (i.e. lists all changes).    */
DECL|method|query ()
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|query
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|query (QueryParameter queryParameter)
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|query
parameter_list|(
name|QueryParameter
name|queryParameter
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|class|QueryParameter
specifier|public
class|class
name|QueryParameter
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
DECL|method|QueryParameter ()
specifier|public
name|QueryParameter
parameter_list|()
block|{}
DECL|method|QueryParameter (String query)
specifier|public
name|QueryParameter
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
block|}
DECL|method|withQuery (String query)
specifier|public
name|QueryParameter
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
name|QueryParameter
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
name|QueryParameter
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
DECL|method|withOption (ListChangesOption options)
specifier|public
name|QueryParameter
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
DECL|method|withOptions (ListChangesOption... options)
specifier|public
name|QueryParameter
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
DECL|method|withOptions (EnumSet<ListChangesOption> options)
specifier|public
name|QueryParameter
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
block|}
comment|/**    * A default implementation which allows source compatibility    * when adding new methods to the interface.    **/
DECL|class|NotImplemented
specifier|public
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
DECL|method|query ()
specifier|public
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|query
parameter_list|()
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
DECL|method|query (QueryParameter queryParameter)
specifier|public
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|query
parameter_list|(
name|QueryParameter
name|queryParameter
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
block|}
block|}
end_interface

end_unit


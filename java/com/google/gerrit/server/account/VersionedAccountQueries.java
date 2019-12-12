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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|entities
operator|.
name|Account
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
name|entities
operator|.
name|RefNames
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
name|server
operator|.
name|git
operator|.
name|ValidationError
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
name|server
operator|.
name|git
operator|.
name|meta
operator|.
name|VersionedMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|CommitBuilder
import|;
end_import

begin_comment
comment|/**  * Named Queries for user accounts.  *  *<p>Users can define aliases for change queries. These are stored as versioned account data and  * (de)serialized with this class.  */
end_comment

begin_class
DECL|class|VersionedAccountQueries
specifier|public
class|class
name|VersionedAccountQueries
extends|extends
name|VersionedMetaData
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|method|forUser (Account.Id id)
specifier|public
specifier|static
name|VersionedAccountQueries
name|forUser
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
operator|new
name|VersionedAccountQueries
argument_list|(
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|queryList
specifier|private
name|QueryList
name|queryList
decl_stmt|;
DECL|method|VersionedAccountQueries (String ref)
specifier|private
name|VersionedAccountQueries
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|protected
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|getQueryList ()
specifier|public
name|QueryList
name|getQueryList
parameter_list|()
block|{
return|return
name|queryList
return|;
block|}
DECL|method|setQueryList (String text)
specifier|public
name|void
name|setQueryList
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|QueryList
name|newQueryList
init|=
name|QueryList
operator|.
name|parse
argument_list|(
name|text
argument_list|,
name|error
lambda|->
name|errors
operator|.
name|add
argument_list|(
name|error
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|errors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|messages
init|=
name|errors
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ValidationError
operator|::
name|getMessage
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|", "
argument_list|)
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"Invalid named queries: "
operator|+
name|messages
argument_list|)
throw|;
block|}
name|queryList
operator|=
name|newQueryList
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|queryList
operator|=
name|QueryList
operator|.
name|parse
argument_list|(
name|readUTF8
argument_list|(
name|QueryList
operator|.
name|FILE_NAME
argument_list|)
argument_list|,
name|error
lambda|->
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Error parsing file %s: %s"
argument_list|,
name|QueryList
operator|.
name|FILE_NAME
argument_list|,
name|error
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSave (CommitBuilder commit)
specifier|protected
name|boolean
name|onSave
parameter_list|(
name|CommitBuilder
name|commit
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|commit
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|commit
operator|.
name|setMessage
argument_list|(
literal|"Updated named queries\n"
argument_list|)
expr_stmt|;
block|}
name|saveUTF8
argument_list|(
name|QueryList
operator|.
name|FILE_NAME
argument_list|,
name|queryList
operator|.
name|asText
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


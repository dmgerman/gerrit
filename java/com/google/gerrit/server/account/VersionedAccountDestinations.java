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
name|FileMode
import|;
end_import

begin_comment
comment|/** User configured named destinations. */
end_comment

begin_class
DECL|class|VersionedAccountDestinations
specifier|public
class|class
name|VersionedAccountDestinations
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
name|VersionedAccountDestinations
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
name|VersionedAccountDestinations
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
DECL|field|destinations
specifier|private
specifier|final
name|DestinationList
name|destinations
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
DECL|method|VersionedAccountDestinations (String ref)
specifier|private
name|VersionedAccountDestinations
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
DECL|method|getDestinationList ()
specifier|public
name|DestinationList
name|getDestinationList
parameter_list|()
block|{
return|return
name|destinations
return|;
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
if|if
condition|(
name|revision
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|prefix
init|=
name|DestinationList
operator|.
name|DIR_NAME
operator|+
literal|"/"
decl_stmt|;
for|for
control|(
name|PathInfo
name|p
range|:
name|getPathInfos
argument_list|(
literal|true
argument_list|)
control|)
block|{
if|if
condition|(
name|p
operator|.
name|fileMode
operator|==
name|FileMode
operator|.
name|REGULAR_FILE
condition|)
block|{
name|String
name|path
init|=
name|p
operator|.
name|path
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|String
name|label
init|=
name|path
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|destinations
operator|.
name|parseLabel
argument_list|(
name|label
argument_list|,
name|readUTF8
argument_list|(
name|path
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
name|path
argument_list|,
name|error
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot yet save destinations"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit


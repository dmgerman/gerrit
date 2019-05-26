begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|Config
import|;
end_import

begin_comment
comment|/** Preferences for user accounts during schema migrations. */
end_comment

begin_class
DECL|class|VersionedAccountPreferences
class|class
name|VersionedAccountPreferences
extends|extends
name|VersionedMetaData
block|{
DECL|field|PREFERENCES
specifier|static
specifier|final
name|String
name|PREFERENCES
init|=
literal|"preferences.config"
decl_stmt|;
DECL|method|forUser (Account.Id id)
specifier|static
name|VersionedAccountPreferences
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
name|VersionedAccountPreferences
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
DECL|method|forDefault ()
specifier|static
name|VersionedAccountPreferences
name|forDefault
parameter_list|()
block|{
return|return
operator|new
name|VersionedAccountPreferences
argument_list|(
name|RefNames
operator|.
name|REFS_USERS_DEFAULT
argument_list|)
return|;
block|}
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|cfg
specifier|private
name|Config
name|cfg
decl_stmt|;
DECL|method|VersionedAccountPreferences (String ref)
specifier|protected
name|VersionedAccountPreferences
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
DECL|method|getConfig ()
name|Config
name|getConfig
parameter_list|()
block|{
return|return
name|cfg
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
name|cfg
operator|=
name|readConfig
argument_list|(
name|PREFERENCES
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
literal|"Updated preferences\n"
argument_list|)
expr_stmt|;
block|}
name|saveConfig
argument_list|(
name|PREFERENCES
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


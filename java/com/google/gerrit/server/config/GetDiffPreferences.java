begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigUtil
operator|.
name|loadSection
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
name|client
operator|.
name|DiffPreferencesInfo
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
name|BadRequestException
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
name|ResourceConflictException
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
name|RestReadView
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
name|account
operator|.
name|VersionedAccountPreferences
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
name|GitRepositoryManager
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
name|UserConfigSections
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|Repository
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GetDiffPreferences
specifier|public
class|class
name|GetDiffPreferences
implements|implements
name|RestReadView
argument_list|<
name|ConfigResource
argument_list|>
block|{
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetDiffPreferences (GitRepositoryManager gitManager, AllUsersName allUsersName)
name|GetDiffPreferences
parameter_list|(
name|GitRepositoryManager
name|gitManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|)
block|{
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource configResource)
specifier|public
name|DiffPreferencesInfo
name|apply
parameter_list|(
name|ConfigResource
name|configResource
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
name|readFromGit
argument_list|(
name|gitManager
argument_list|,
name|allUsersName
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|readFromGit ( GitRepositoryManager gitMgr, AllUsersName allUsersName, DiffPreferencesInfo in)
specifier|static
name|DiffPreferencesInfo
name|readFromGit
parameter_list|(
name|GitRepositoryManager
name|gitMgr
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|DiffPreferencesInfo
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|RepositoryNotFoundException
block|{
try|try
init|(
name|Repository
name|git
init|=
name|gitMgr
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
comment|// Load all users prefs.
name|VersionedAccountPreferences
name|dp
init|=
name|VersionedAccountPreferences
operator|.
name|forDefault
argument_list|()
decl_stmt|;
name|dp
operator|.
name|load
argument_list|(
name|git
argument_list|)
expr_stmt|;
name|DiffPreferencesInfo
name|allUserPrefs
init|=
operator|new
name|DiffPreferencesInfo
argument_list|()
decl_stmt|;
name|loadSection
argument_list|(
name|dp
operator|.
name|getConfig
argument_list|()
argument_list|,
name|UserConfigSections
operator|.
name|DIFF
argument_list|,
literal|null
argument_list|,
name|allUserPrefs
argument_list|,
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
return|return
name|allUserPrefs
return|;
block|}
block|}
block|}
end_class

end_unit


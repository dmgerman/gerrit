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
DECL|package|com.google.gerrit.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
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
name|server
operator|.
name|IdentifiedUser
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
name|config
operator|.
name|AllUsersName
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
name|assistedinject
operator|.
name|Assisted
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
name|lib
operator|.
name|Repository
import|;
end_import

begin_class
DECL|class|GerritPushCertificateChecker
specifier|public
class|class
name|GerritPushCertificateChecker
extends|extends
name|PushCertificateChecker
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (IdentifiedUser expectedUser)
name|GerritPushCertificateChecker
name|create
parameter_list|(
name|IdentifiedUser
name|expectedUser
parameter_list|)
function_decl|;
block|}
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsers
specifier|private
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritPushCertificateChecker ( GerritPublicKeyChecker.Factory keyCheckerFactory, GitRepositoryManager repoManager, AllUsersName allUsers, @Assisted IdentifiedUser expectedUser)
name|GerritPushCertificateChecker
parameter_list|(
name|GerritPublicKeyChecker
operator|.
name|Factory
name|keyCheckerFactory
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|expectedUser
parameter_list|)
block|{
name|super
argument_list|(
name|keyCheckerFactory
operator|.
name|create
argument_list|()
operator|.
name|setExpectedUser
argument_list|(
name|expectedUser
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allUsers
operator|=
name|allUsers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRepository ()
specifier|protected
name|Repository
name|getRepository
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsers
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|shouldClose (Repository repo)
specifier|protected
name|boolean
name|shouldClose
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


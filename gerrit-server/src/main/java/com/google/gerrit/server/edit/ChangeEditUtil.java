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
DECL|package|com.google.gerrit.server.edit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|edit
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
name|Optional
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
name|AuthException
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|reviewdb
operator|.
name|client
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
name|Provider
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Ref
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Utility functions to manipulate change edits.  *<p>  * This class contains method to retrieve edits.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeEditUtil
specifier|public
class|class
name|ChangeEditUtil
block|{
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeEditUtil (GitRepositoryManager gitManager, Provider<IdentifiedUser> user)
name|ChangeEditUtil
parameter_list|(
name|GitRepositoryManager
name|gitManager
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|user
parameter_list|)
block|{
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
comment|/**    * Retrieve edits for a change and user. Max. one change edit can    * exist per user and change.    *    * @param change    * @return edit for this change for this user, if present.    * @throws AuthException    * @throws IOException    */
DECL|method|byChange (Change change)
specifier|public
name|Optional
argument_list|<
name|ChangeEdit
argument_list|>
name|byChange
parameter_list|(
name|Change
name|change
parameter_list|)
throws|throws
name|AuthException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|user
operator|.
name|get
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
name|Repository
name|repo
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|IdentifiedUser
name|identifiedUser
init|=
operator|(
name|IdentifiedUser
operator|)
name|user
operator|.
name|get
argument_list|()
decl_stmt|;
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRef
argument_list|(
name|editRefName
argument_list|(
name|identifiedUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|ChangeEdit
argument_list|(
name|identifiedUser
argument_list|,
name|change
argument_list|,
name|ref
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Returns reference for this change edit with sharded user and change number:    * refs/users/UU/UUUU/edit-CCCC.    *    * @param accountId accout id    * @param changeId change number    * @return reference for this change edit    */
DECL|method|editRefName (Account.Id accountId, Change.Id changeId)
specifier|static
name|String
name|editRefName
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s/edit-%d"
argument_list|,
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|accountId
argument_list|)
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


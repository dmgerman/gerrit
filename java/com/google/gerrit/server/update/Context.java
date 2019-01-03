begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|Project
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
name|CurrentUser
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
name|account
operator|.
name|AccountState
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_comment
comment|/**  * Context for performing a {@link BatchUpdate}.  *  *<p>A single update may span multiple changes, but they all belong to a single repo.  */
end_comment

begin_interface
DECL|interface|Context
specifier|public
interface|interface
name|Context
block|{
comment|/**    * Get the project name this update operates on.    *    * @return project.    */
DECL|method|getProject ()
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
function_decl|;
comment|/**    * Get a read-only view of the open repository for this project.    *    *<p>Will be opened lazily if necessary.    *    * @return repository instance.    * @throws IOException if an error occurred opening the repo.    */
DECL|method|getRepoView ()
name|RepoView
name|getRepoView
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Get a walk for this project.    *    *<p>The repository will be opened lazily if necessary; callers should not close the walk.    *    * @return walk.    * @throws IOException if an error occurred opening the repo.    */
DECL|method|getRevWalk ()
name|RevWalk
name|getRevWalk
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Get the timestamp at which this update takes place.    *    * @return timestamp.    */
DECL|method|getWhen ()
name|Timestamp
name|getWhen
parameter_list|()
function_decl|;
comment|/**    * Get the time zone in which this update takes place.    *    *<p>In the current implementation, this is always the time zone of the server.    *    * @return time zone.    */
DECL|method|getTimeZone ()
name|TimeZone
name|getTimeZone
parameter_list|()
function_decl|;
comment|/**    * Get the user performing the update.    *    *<p>In the current implementation, this is always an {@link IdentifiedUser} or {@link    * com.google.gerrit.server.InternalUser}.    *    * @return user.    */
DECL|method|getUser ()
name|CurrentUser
name|getUser
parameter_list|()
function_decl|;
comment|/**    * Get the identified user performing the update.    *    *<p>Convenience method for {@code getUser().asIdentifiedUser()}.    *    * @see CurrentUser#asIdentifiedUser()    * @return user.    */
DECL|method|getIdentifiedUser ()
specifier|default
name|IdentifiedUser
name|getIdentifiedUser
parameter_list|()
block|{
return|return
name|requireNonNull
argument_list|(
name|getUser
argument_list|()
argument_list|)
operator|.
name|asIdentifiedUser
argument_list|()
return|;
block|}
comment|/**    * Get the account of the user performing the update.    *    *<p>Convenience method for {@code getIdentifiedUser().getAccount()}.    *    * @see CurrentUser#asIdentifiedUser()    * @return account.    */
DECL|method|getAccount ()
specifier|default
name|AccountState
name|getAccount
parameter_list|()
block|{
return|return
name|getIdentifiedUser
argument_list|()
operator|.
name|state
argument_list|()
return|;
block|}
comment|/**    * Get the account ID of the user performing the update.    *    *<p>Convenience method for {@code getUser().getAccountId()}    *    * @see CurrentUser#getAccountId()    * @return account ID.    */
DECL|method|getAccountId ()
specifier|default
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|getIdentifiedUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
return|;
block|}
block|}
end_interface

end_unit


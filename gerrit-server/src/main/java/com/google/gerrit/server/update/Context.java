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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|lib
operator|.
name|Repository
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
comment|/** @return the project name this update operates on. */
DECL|method|getProject ()
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
function_decl|;
comment|/**    * Get an open repository instance for this project.    *    *<p>Will be opened lazily if necessary; callers should not close the repo. In some phases of the    * update, the repository might be read-only; see {@link BatchUpdate.Op} for details.    *    * @return repository instance.    * @throws IOException if an error occurred opening the repo.    */
DECL|method|getRepository ()
name|Repository
name|getRepository
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
comment|/** @return the timestamp at which this update takes place. */
DECL|method|getWhen ()
name|Timestamp
name|getWhen
parameter_list|()
function_decl|;
comment|/**    * @return the time zone in which this update takes place. In the current implementation, this is    *     always the time zone of the server.    */
DECL|method|getTimeZone ()
name|TimeZone
name|getTimeZone
parameter_list|()
function_decl|;
comment|/**    * @return an open ReviewDb database. Callers should not manage transactions or call mutating    *     methods on the Changes table. Mutations on other tables (including other entities in the    *     change entity group) are fine.    */
DECL|method|getDb ()
name|ReviewDb
name|getDb
parameter_list|()
function_decl|;
comment|/**    * @return user performing the update. In the current implementation, this is always an {@link    *     IdentifiedUser} or {@link com.google.gerrit.server.InternalUser}.    */
DECL|method|getUser ()
name|CurrentUser
name|getUser
parameter_list|()
function_decl|;
comment|/** @return order in which operations are executed in this update. */
DECL|method|getOrder ()
name|Order
name|getOrder
parameter_list|()
function_decl|;
comment|/**    * @return identified user performing the update; throws an unchecked exception if the user is not    *     an {@link IdentifiedUser}    */
DECL|method|getIdentifiedUser ()
specifier|default
name|IdentifiedUser
name|getIdentifiedUser
parameter_list|()
block|{
return|return
name|checkNotNull
argument_list|(
name|getUser
argument_list|()
argument_list|)
operator|.
name|asIdentifiedUser
argument_list|()
return|;
block|}
comment|/**    * @return account of the user performing the update; throws if the user is not an {@link    *     IdentifiedUser}    */
DECL|method|getAccount ()
specifier|default
name|Account
name|getAccount
parameter_list|()
block|{
return|return
name|getIdentifiedUser
argument_list|()
operator|.
name|getAccount
argument_list|()
return|;
block|}
comment|/**    * @return account ID of the user performing the update; throws if the user is not an {@link    *     IdentifiedUser}    */
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


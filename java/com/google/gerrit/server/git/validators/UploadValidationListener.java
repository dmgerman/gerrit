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
DECL|package|com.google.gerrit.server.git.validators
package|package
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
name|validators
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
name|entities
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|validators
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|ObjectId
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
name|transport
operator|.
name|UploadPack
import|;
end_import

begin_comment
comment|/**  * Listener to provide validation for upload operations.  *  *<p>Invoked by Gerrit before it begins to send a pack to the client.  *  *<p>Implementors can block the upload operation by throwing a ValidationException. The exception's  * message text will be reported to the end-user over the client's protocol connection.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|UploadValidationListener
specifier|public
interface|interface
name|UploadValidationListener
block|{
comment|/**    * Validate an upload before it begins.    *    * @param repository The repository    * @param project The project    * @param remoteHost Remote address/hostname of the user    * @param up the UploadPack instance being processed.    * @param wants The list of wanted objects. These may be RevObject or RevCommit if the processor    *     parsed them. Implementors should not rely on the values being parsed.    * @param haves The list of common objects. Empty on an initial clone request. These may be    *     RevObject or RevCommit if the processor parsed them. Implementors should not rely on the    *     values being parsed.    * @throws ValidationException to block the upload and send a message back to the end-user over    *     the client's protocol connection.    */
DECL|method|onPreUpload ( Repository repository, Project project, String remoteHost, UploadPack up, Collection<? extends ObjectId> wants, Collection<? extends ObjectId> haves)
specifier|default
name|void
name|onPreUpload
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|Project
name|project
parameter_list|,
name|String
name|remoteHost
parameter_list|,
name|UploadPack
name|up
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|ObjectId
argument_list|>
name|wants
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|ObjectId
argument_list|>
name|haves
parameter_list|)
throws|throws
name|ValidationException
block|{}
comment|/**    * Invoked before negotiation round is started.    *    * @param repository The repository    * @param project The project    * @param remoteHost Remote address/hostname of the user    * @param up the UploadPack instance being processed    * @param wants The list of wanted objects. These may be RevObject or RevCommit if the processor    *     parsed them. Implementors should not rely on the values being parsed.    * @param cntOffered number of objects the client has offered.    * @throws ValidationException to block the upload and send a message back to the end-user over    *     the client's protocol connection.    */
DECL|method|onBeginNegotiate ( Repository repository, Project project, String remoteHost, UploadPack up, Collection<? extends ObjectId> wants, int cntOffered)
specifier|default
name|void
name|onBeginNegotiate
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|Project
name|project
parameter_list|,
name|String
name|remoteHost
parameter_list|,
name|UploadPack
name|up
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|ObjectId
argument_list|>
name|wants
parameter_list|,
name|int
name|cntOffered
parameter_list|)
throws|throws
name|ValidationException
block|{}
block|}
end_interface

end_unit


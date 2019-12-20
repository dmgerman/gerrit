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
name|client
operator|.
name|ChangeEditDetailOption
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
name|EditInfo
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
name|BinaryResult
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
name|RawInput
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
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * An API for the change edit of a change. A change edit is similar to a patch set and will become  * one if it is published (by {@link #publish(PublishChangeEditInput)}). Whenever the descriptions  * below refer to files of a change edit, they actually refer to the files of the Git tree which is  * represented by the change edit. A change can have at most one change edit at each point in time.  */
end_comment

begin_interface
DECL|interface|ChangeEditApi
specifier|public
interface|interface
name|ChangeEditApi
block|{
DECL|class|ChangeEditDetailRequest
specifier|abstract
class|class
name|ChangeEditDetailRequest
block|{
DECL|field|base
specifier|private
name|String
name|base
decl_stmt|;
DECL|field|options
specifier|private
name|EnumSet
argument_list|<
name|ChangeEditDetailOption
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ChangeEditDetailOption
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|EditInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|withBase (String base)
specifier|public
name|ChangeEditDetailRequest
name|withBase
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withOption (ChangeEditDetailOption option)
specifier|public
name|ChangeEditDetailRequest
name|withOption
parameter_list|(
name|ChangeEditDetailOption
name|option
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getBase ()
specifier|public
name|String
name|getBase
parameter_list|()
block|{
return|return
name|base
return|;
block|}
DECL|method|options ()
specifier|public
name|EnumSet
argument_list|<
name|ChangeEditDetailOption
argument_list|>
name|options
parameter_list|()
block|{
return|return
name|options
return|;
block|}
block|}
DECL|method|detail ()
name|ChangeEditDetailRequest
name|detail
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Retrieves details regarding the change edit.    *    * @return an {@code Optional} containing details about the change edit if it exists, or {@code    *     Optional.empty()}    * @throws RestApiException if the change edit couldn't be retrieved    */
DECL|method|get ()
name|Optional
argument_list|<
name|EditInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Creates a new change edit. It has exactly the same Git tree as the current patch set of the    * change.    *    * @throws RestApiException if the change edit couldn't be created or a change edit already exists    */
DECL|method|create ()
name|void
name|create
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Deletes the change edit.    *    * @throws RestApiException if the change edit couldn't be deleted or a change edit wasn't present    */
DECL|method|delete ()
name|void
name|delete
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Rebases the change edit on top of the latest patch set of this change.    *    * @throws RestApiException if the change edit couldn't be rebased or a change edit wasn't present    */
DECL|method|rebase ()
name|void
name|rebase
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Publishes the change edit using default settings. See {@link #publish(PublishChangeEditInput)}    * for more details.    *    * @throws RestApiException if the change edit couldn't be published or a change edit wasn't    *     present    */
DECL|method|publish ()
name|void
name|publish
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Publishes the change edit. Publishing means that the change edit is turned into a regular patch    * set of the change.    *    * @param publishChangeEditInput a {@code PublishChangeEditInput} specifying the options which    *     should be applied    * @throws RestApiException if the change edit couldn't be published or a change edit wasn't    *     present    */
DECL|method|publish (PublishChangeEditInput publishChangeEditInput)
name|void
name|publish
parameter_list|(
name|PublishChangeEditInput
name|publishChangeEditInput
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Retrieves the contents of the specified file from the change edit.    *    * @param filePath the path of the file    * @return an {@code Optional} containing the contents of the file as a {@code BinaryResult} if    *     the file exists within the change edit, or {@code Optional.empty()}    * @throws RestApiException if the contents of the file couldn't be retrieved or a change edit    *     wasn't present    */
DECL|method|getFile (String filePath)
name|Optional
argument_list|<
name|BinaryResult
argument_list|>
name|getFile
parameter_list|(
name|String
name|filePath
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Renames a file of the change edit or moves the file to another directory. If the change edit    * doesn't exist, it will be created based on the current patch set of the change.    *    * @param oldFilePath the current file path    * @param newFilePath the desired file path    * @throws RestApiException if the file couldn't be renamed    */
DECL|method|renameFile (String oldFilePath, String newFilePath)
name|void
name|renameFile
parameter_list|(
name|String
name|oldFilePath
parameter_list|,
name|String
name|newFilePath
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Restores a file of the change edit to the state in which it was before the patch set on which    * the change edit is based. This includes the file content as well as the existence or    * non-existence of the file. If the change edit doesn't exist, it will be created based on the    * current patch set of the change.    *    * @param filePath the path of the file    * @throws RestApiException if the file couldn't be restored to its previous state    */
DECL|method|restoreFile (String filePath)
name|void
name|restoreFile
parameter_list|(
name|String
name|filePath
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Modify the contents of the specified file of the change edit. If no content is provided, the    * content of the file is erased but the file isn't deleted. If the change edit doesn't exist, it    * will be created based on the current patch set of the change.    *    * @param filePath the path of the file which should be modified    * @param newContent the desired content of the file    * @throws RestApiException if the content of the file couldn't be modified    */
DECL|method|modifyFile (String filePath, RawInput newContent)
specifier|default
name|void
name|modifyFile
parameter_list|(
name|String
name|filePath
parameter_list|,
name|RawInput
name|newContent
parameter_list|)
throws|throws
name|RestApiException
block|{
name|FileContentInput
name|input
init|=
operator|new
name|FileContentInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|content
operator|=
name|newContent
expr_stmt|;
name|modifyFile
argument_list|(
name|filePath
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
comment|/**    * Modify the contents of the specified file of the change edit. If no content is provided, the    * content of the file is erased but the file isn't deleted. If the change edit doesn't exist, it    * will be created based on the current patch set of the change.    *    * @param filePath the path of the file which should be modified    * @param input the desired content of the file    * @throws RestApiException if the content of the file couldn't be modified    */
DECL|method|modifyFile (String filePath, FileContentInput input)
name|void
name|modifyFile
parameter_list|(
name|String
name|filePath
parameter_list|,
name|FileContentInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Deletes the specified file from the change edit. If the change edit doesn't exist, it will be    * created based on the current patch set of the change.    *    * @param filePath the path fo the file which should be deleted    * @throws RestApiException if the file couldn't be deleted    */
DECL|method|deleteFile (String filePath)
name|void
name|deleteFile
parameter_list|(
name|String
name|filePath
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Retrieves the commit message of the change edit.    *    * @return the commit message of the change edit    * @throws RestApiException if the commit message couldn't be retrieved or a change edit wasn't    *     present    */
DECL|method|getCommitMessage ()
name|String
name|getCommitMessage
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Modifies the commit message of the change edit. If the change edit doesn't exist, it will be    * created based on the current patch set of the change.    *    * @param newCommitMessage the desired commit message    * @throws RestApiException if the commit message couldn't be modified    */
DECL|method|modifyCommitMessage (String newCommitMessage)
name|void
name|modifyCommitMessage
parameter_list|(
name|String
name|newCommitMessage
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|ChangeEditApi
block|{
annotation|@
name|Override
DECL|method|detail ()
specifier|public
name|ChangeEditDetailRequest
name|detail
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
DECL|method|get ()
specifier|public
name|Optional
argument_list|<
name|EditInfo
argument_list|>
name|get
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
DECL|method|create ()
specifier|public
name|void
name|create
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
DECL|method|delete ()
specifier|public
name|void
name|delete
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
DECL|method|rebase ()
specifier|public
name|void
name|rebase
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
DECL|method|publish ()
specifier|public
name|void
name|publish
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
DECL|method|publish (PublishChangeEditInput publishChangeEditInput)
specifier|public
name|void
name|publish
parameter_list|(
name|PublishChangeEditInput
name|publishChangeEditInput
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
DECL|method|getFile (String filePath)
specifier|public
name|Optional
argument_list|<
name|BinaryResult
argument_list|>
name|getFile
parameter_list|(
name|String
name|filePath
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
DECL|method|renameFile (String oldFilePath, String newFilePath)
specifier|public
name|void
name|renameFile
parameter_list|(
name|String
name|oldFilePath
parameter_list|,
name|String
name|newFilePath
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
DECL|method|restoreFile (String filePath)
specifier|public
name|void
name|restoreFile
parameter_list|(
name|String
name|filePath
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
DECL|method|modifyFile (String filePath, FileContentInput input)
specifier|public
name|void
name|modifyFile
parameter_list|(
name|String
name|filePath
parameter_list|,
name|FileContentInput
name|input
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
DECL|method|deleteFile (String filePath)
specifier|public
name|void
name|deleteFile
parameter_list|(
name|String
name|filePath
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
DECL|method|getCommitMessage ()
specifier|public
name|String
name|getCommitMessage
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
DECL|method|modifyCommitMessage (String newCommitMessage)
specifier|public
name|void
name|modifyCommitMessage
parameter_list|(
name|String
name|newCommitMessage
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


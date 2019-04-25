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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
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
name|PureRevertCache
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
name|notedb
operator|.
name|ChangeNotes
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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|InvalidObjectIdException
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

begin_comment
comment|/** Can check if a change is a pure revert (= a revert with no further modifications). */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PureRevert
specifier|public
class|class
name|PureRevert
block|{
DECL|field|pureRevertCache
specifier|private
specifier|final
name|PureRevertCache
name|pureRevertCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|PureRevert (PureRevertCache pureRevertCache)
name|PureRevert
parameter_list|(
name|PureRevertCache
name|pureRevertCache
parameter_list|)
block|{
name|this
operator|.
name|pureRevertCache
operator|=
name|pureRevertCache
expr_stmt|;
block|}
DECL|method|get (ChangeNotes notes, Optional<String> claimedOriginal)
specifier|public
name|boolean
name|get
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|Optional
argument_list|<
name|String
argument_list|>
name|claimedOriginal
parameter_list|)
throws|throws
name|IOException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
block|{
name|PatchSet
name|currentPatchSet
init|=
name|notes
operator|.
name|getCurrentPatchSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentPatchSet
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"current revision is missing"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|claimedOriginal
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|pureRevertCache
operator|.
name|isPureRevert
argument_list|(
name|notes
argument_list|)
return|;
block|}
name|ObjectId
name|claimedOriginalObjectId
decl_stmt|;
try|try
block|{
name|claimedOriginalObjectId
operator|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|claimedOriginal
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidObjectIdException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"invalid object ID"
argument_list|)
throw|;
block|}
return|return
name|pureRevertCache
operator|.
name|isPureRevert
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|,
name|notes
operator|.
name|getCurrentPatchSet
argument_list|()
operator|.
name|commitId
argument_list|()
argument_list|,
name|claimedOriginalObjectId
argument_list|)
return|;
block|}
block|}
end_class

end_unit


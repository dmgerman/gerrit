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
name|ResourceNotFoundException
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
name|reviewdb
operator|.
name|client
operator|.
name|Patch
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GetContentType
specifier|public
class|class
name|GetContentType
implements|implements
name|RestReadView
argument_list|<
name|FileResource
argument_list|>
block|{
DECL|field|fileContentUtil
specifier|private
specifier|final
name|FileContentUtil
name|fileContentUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetContentType (FileContentUtil fileContentUtil)
name|GetContentType
parameter_list|(
name|FileContentUtil
name|fileContentUtil
parameter_list|)
block|{
name|this
operator|.
name|fileContentUtil
operator|=
name|fileContentUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (FileResource rsrc)
specifier|public
name|String
name|apply
parameter_list|(
name|FileResource
name|rsrc
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
block|{
name|String
name|path
init|=
name|rsrc
operator|.
name|getPatchKey
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|FileContentUtil
operator|.
name|TEXT_X_GERRIT_COMMIT_MESSAGE
return|;
block|}
return|return
name|fileContentUtil
operator|.
name|getContentType
argument_list|(
name|rsrc
operator|.
name|getRevision
argument_list|()
operator|.
name|getControl
argument_list|()
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
name|rsrc
operator|.
name|getRevision
argument_list|()
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|path
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.edit.tree
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
operator|.
name|tree
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
name|truth
operator|.
name|Truth
operator|.
name|assertAbout
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|CharStreams
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|FailureMetadata
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|StringSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Subject
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_class
DECL|class|ChangeFileContentModificationSubject
specifier|public
class|class
name|ChangeFileContentModificationSubject
extends|extends
name|Subject
argument_list|<
name|ChangeFileContentModificationSubject
argument_list|,
name|ChangeFileContentModification
argument_list|>
block|{
DECL|method|assertThat ( ChangeFileContentModification modification)
specifier|public
specifier|static
name|ChangeFileContentModificationSubject
name|assertThat
parameter_list|(
name|ChangeFileContentModification
name|modification
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|modifications
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|modification
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Factory
argument_list|<
name|ChangeFileContentModificationSubject
argument_list|,
name|ChangeFileContentModification
argument_list|>
DECL|method|modifications ()
name|modifications
parameter_list|()
block|{
return|return
name|ChangeFileContentModificationSubject
operator|::
operator|new
return|;
block|}
DECL|field|modification
specifier|private
specifier|final
name|ChangeFileContentModification
name|modification
decl_stmt|;
DECL|method|ChangeFileContentModificationSubject ( FailureMetadata failureMetadata, ChangeFileContentModification modification)
specifier|private
name|ChangeFileContentModificationSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|ChangeFileContentModification
name|modification
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|modification
argument_list|)
expr_stmt|;
name|this
operator|.
name|modification
operator|=
name|modification
expr_stmt|;
block|}
DECL|method|filePath ()
specifier|public
name|StringSubject
name|filePath
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getFilePath()"
argument_list|)
operator|.
name|that
argument_list|(
name|modification
operator|.
name|getFilePath
argument_list|()
argument_list|)
return|;
block|}
DECL|method|newContent ()
specifier|public
name|StringSubject
name|newContent
parameter_list|()
throws|throws
name|IOException
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|RawInput
name|newContent
init|=
name|modification
operator|.
name|getNewContent
argument_list|()
decl_stmt|;
name|check
argument_list|(
literal|"getNewContent()"
argument_list|)
operator|.
name|that
argument_list|(
name|newContent
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|String
name|contentString
init|=
name|CharStreams
operator|.
name|toString
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|newContent
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|check
argument_list|(
literal|"getNewContent()"
argument_list|)
operator|.
name|that
argument_list|(
name|contentString
argument_list|)
return|;
block|}
block|}
end_class

end_unit


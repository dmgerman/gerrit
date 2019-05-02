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
name|truth
operator|.
name|ListSubject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|TreeModificationSubject
specifier|public
class|class
name|TreeModificationSubject
extends|extends
name|Subject
argument_list|<
name|TreeModificationSubject
argument_list|,
name|TreeModification
argument_list|>
block|{
DECL|method|assertThat (TreeModification treeModification)
specifier|public
specifier|static
name|TreeModificationSubject
name|assertThat
parameter_list|(
name|TreeModification
name|treeModification
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|treeModifications
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|treeModification
argument_list|)
return|;
block|}
DECL|method|treeModifications ()
specifier|private
specifier|static
name|Factory
argument_list|<
name|TreeModificationSubject
argument_list|,
name|TreeModification
argument_list|>
name|treeModifications
parameter_list|()
block|{
return|return
name|TreeModificationSubject
operator|::
operator|new
return|;
block|}
DECL|method|assertThatList ( List<TreeModification> treeModifications)
specifier|public
specifier|static
name|ListSubject
argument_list|<
name|TreeModificationSubject
argument_list|,
name|TreeModification
argument_list|>
name|assertThatList
parameter_list|(
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|ListSubject
operator|.
name|elements
argument_list|()
argument_list|)
operator|.
name|thatCustom
argument_list|(
name|treeModifications
argument_list|,
name|treeModifications
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"treeModifications"
argument_list|)
return|;
block|}
DECL|method|TreeModificationSubject ( FailureMetadata failureMetadata, TreeModification treeModification)
specifier|private
name|TreeModificationSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|TreeModification
name|treeModification
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|treeModification
argument_list|)
expr_stmt|;
block|}
DECL|method|asChangeFileContentModification ()
specifier|public
name|ChangeFileContentModificationSubject
name|asChangeFileContentModification
parameter_list|()
block|{
name|isInstanceOf
argument_list|(
name|ChangeFileContentModification
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|check
argument_list|(
literal|"asChangeFileContentModification()"
argument_list|)
operator|.
name|about
argument_list|(
name|ChangeFileContentModificationSubject
operator|.
name|modifications
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
operator|(
name|ChangeFileContentModification
operator|)
name|actual
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


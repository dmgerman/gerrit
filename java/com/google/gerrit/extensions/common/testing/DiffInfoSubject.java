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
DECL|package|com.google.gerrit.extensions.common.testing
package|package
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
name|testing
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
import|import static
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
name|testing
operator|.
name|FileMetaSubject
operator|.
name|fileMetas
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|truth
operator|.
name|ListSubject
operator|.
name|elements
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
name|ComparableSubject
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
name|extensions
operator|.
name|common
operator|.
name|ChangeType
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
name|DiffInfo
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
name|DiffInfo
operator|.
name|ContentEntry
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

begin_class
DECL|class|DiffInfoSubject
specifier|public
class|class
name|DiffInfoSubject
extends|extends
name|Subject
argument_list|<
name|DiffInfoSubject
argument_list|,
name|DiffInfo
argument_list|>
block|{
DECL|method|assertThat (DiffInfo diffInfo)
specifier|public
specifier|static
name|DiffInfoSubject
name|assertThat
parameter_list|(
name|DiffInfo
name|diffInfo
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|DiffInfoSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|diffInfo
argument_list|)
return|;
block|}
DECL|method|DiffInfoSubject (FailureMetadata failureMetadata, DiffInfo diffInfo)
specifier|private
name|DiffInfoSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|DiffInfo
name|diffInfo
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|diffInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|content ()
specifier|public
name|ListSubject
argument_list|<
name|ContentEntrySubject
argument_list|,
name|ContentEntry
argument_list|>
name|content
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|DiffInfo
name|diffInfo
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|check
argument_list|(
literal|"content()"
argument_list|)
operator|.
name|about
argument_list|(
name|elements
argument_list|()
argument_list|)
operator|.
name|thatCustom
argument_list|(
name|diffInfo
operator|.
name|content
argument_list|,
name|ContentEntrySubject
operator|.
name|contentEntries
argument_list|()
argument_list|)
return|;
block|}
DECL|method|changeType ()
specifier|public
name|ComparableSubject
argument_list|<
name|?
argument_list|,
name|ChangeType
argument_list|>
name|changeType
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|DiffInfo
name|diffInfo
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|check
argument_list|(
literal|"changeType()"
argument_list|)
operator|.
name|that
argument_list|(
name|diffInfo
operator|.
name|changeType
argument_list|)
return|;
block|}
DECL|method|metaA ()
specifier|public
name|FileMetaSubject
name|metaA
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|DiffInfo
name|diffInfo
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|check
argument_list|(
literal|"metaA()"
argument_list|)
operator|.
name|about
argument_list|(
name|fileMetas
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|diffInfo
operator|.
name|metaA
argument_list|)
return|;
block|}
DECL|method|metaB ()
specifier|public
name|FileMetaSubject
name|metaB
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|DiffInfo
name|diffInfo
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|check
argument_list|(
literal|"metaB()"
argument_list|)
operator|.
name|about
argument_list|(
name|fileMetas
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|diffInfo
operator|.
name|metaB
argument_list|)
return|;
block|}
block|}
end_class

end_unit


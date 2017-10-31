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
name|common
operator|.
name|truth
operator|.
name|Truth
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
DECL|class|ContentEntrySubject
specifier|public
class|class
name|ContentEntrySubject
extends|extends
name|Subject
argument_list|<
name|ContentEntrySubject
argument_list|,
name|ContentEntry
argument_list|>
block|{
DECL|method|assertThat (ContentEntry contentEntry)
specifier|public
specifier|static
name|ContentEntrySubject
name|assertThat
parameter_list|(
name|ContentEntry
name|contentEntry
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|ContentEntrySubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|contentEntry
argument_list|)
return|;
block|}
DECL|method|ContentEntrySubject (FailureMetadata failureMetadata, ContentEntry contentEntry)
specifier|private
name|ContentEntrySubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|ContentEntry
name|contentEntry
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|contentEntry
argument_list|)
expr_stmt|;
block|}
DECL|method|isDueToRebase ()
specifier|public
name|void
name|isDueToRebase
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ContentEntry
name|contentEntry
init|=
name|actual
argument_list|()
decl_stmt|;
name|Truth
operator|.
name|assertWithMessage
argument_list|(
literal|"Entry should be marked 'dueToRebase'"
argument_list|)
operator|.
name|that
argument_list|(
name|contentEntry
operator|.
name|dueToRebase
argument_list|)
operator|.
name|named
argument_list|(
literal|"dueToRebase"
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|isNotDueToRebase ()
specifier|public
name|void
name|isNotDueToRebase
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ContentEntry
name|contentEntry
init|=
name|actual
argument_list|()
decl_stmt|;
name|Truth
operator|.
name|assertWithMessage
argument_list|(
literal|"Entry should not be marked 'dueToRebase'"
argument_list|)
operator|.
name|that
argument_list|(
name|contentEntry
operator|.
name|dueToRebase
argument_list|)
operator|.
name|named
argument_list|(
literal|"dueToRebase"
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
DECL|method|commonLines ()
specifier|public
name|ListSubject
argument_list|<
name|StringSubject
argument_list|,
name|String
argument_list|>
name|commonLines
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ContentEntry
name|contentEntry
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|ListSubject
operator|.
name|assertThat
argument_list|(
name|contentEntry
operator|.
name|ab
argument_list|,
name|Truth
operator|::
name|assertThat
argument_list|)
operator|.
name|named
argument_list|(
literal|"common lines"
argument_list|)
return|;
block|}
DECL|method|linesOfA ()
specifier|public
name|ListSubject
argument_list|<
name|StringSubject
argument_list|,
name|String
argument_list|>
name|linesOfA
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ContentEntry
name|contentEntry
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|ListSubject
operator|.
name|assertThat
argument_list|(
name|contentEntry
operator|.
name|a
argument_list|,
name|Truth
operator|::
name|assertThat
argument_list|)
operator|.
name|named
argument_list|(
literal|"lines of 'a'"
argument_list|)
return|;
block|}
DECL|method|linesOfB ()
specifier|public
name|ListSubject
argument_list|<
name|StringSubject
argument_list|,
name|String
argument_list|>
name|linesOfB
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ContentEntry
name|contentEntry
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|ListSubject
operator|.
name|assertThat
argument_list|(
name|contentEntry
operator|.
name|b
argument_list|,
name|Truth
operator|::
name|assertThat
argument_list|)
operator|.
name|named
argument_list|(
literal|"lines of 'b'"
argument_list|)
return|;
block|}
block|}
end_class

end_unit


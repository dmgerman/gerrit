begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|RefNames
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
name|FooterKey
import|;
end_import

begin_class
DECL|class|ChangeNoteUtil
specifier|public
class|class
name|ChangeNoteUtil
block|{
DECL|field|GERRIT_PLACEHOLDER_HOST
specifier|static
specifier|final
name|String
name|GERRIT_PLACEHOLDER_HOST
init|=
literal|"gerrit"
decl_stmt|;
DECL|field|FOOTER_LABEL
specifier|static
specifier|final
name|FooterKey
name|FOOTER_LABEL
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Label"
argument_list|)
decl_stmt|;
DECL|field|FOOTER_PATCH_SET
specifier|static
specifier|final
name|FooterKey
name|FOOTER_PATCH_SET
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Patch-set"
argument_list|)
decl_stmt|;
DECL|field|FOOTER_STATUS
specifier|static
specifier|final
name|FooterKey
name|FOOTER_STATUS
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Status"
argument_list|)
decl_stmt|;
DECL|field|FOOTER_SUBMITTED_WITH
specifier|static
specifier|final
name|FooterKey
name|FOOTER_SUBMITTED_WITH
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Submitted-with"
argument_list|)
decl_stmt|;
DECL|method|changeRefName (Change.Id id)
specifier|public
specifier|static
name|String
name|changeRefName
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|RefNames
operator|.
name|REFS_CHANGES
argument_list|)
expr_stmt|;
name|int
name|n
init|=
name|id
operator|.
name|get
argument_list|()
decl_stmt|;
name|int
name|m
init|=
name|n
operator|%
literal|100
decl_stmt|;
if|if
condition|(
name|m
operator|<
literal|10
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|"/meta"
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|ChangeNoteUtil ()
specifier|private
name|ChangeNoteUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit


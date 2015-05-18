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
DECL|package|com.google.gerrit.acceptance.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|change
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
name|assertThat
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|pushHead
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
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|Iterables
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|GitUtil
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
name|acceptance
operator|.
name|PushOneCommit
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
name|acceptance
operator|.
name|RestSession
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
name|CommitInfo
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
name|change
operator|.
name|GetRelated
operator|.
name|ChangeAndCommit
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
name|change
operator|.
name|GetRelated
operator|.
name|RelatedInfo
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
name|edit
operator|.
name|ChangeEditModifier
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
name|edit
operator|.
name|ChangeEditUtil
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|testutil
operator|.
name|ConfigSuite
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
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
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|List
import|;
end_import

begin_class
DECL|class|GetRelatedIT
specifier|public
class|class
name|GetRelatedIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|byGroup ()
specifier|public
specifier|static
name|Config
name|byGroup
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"getRelatedByAncestors"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|byAncestors ()
specifier|public
specifier|static
name|Config
name|byAncestors
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"getRelatedByAncestors"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Inject
DECL|field|editUtil
specifier|private
name|ChangeEditUtil
name|editUtil
decl_stmt|;
annotation|@
name|Inject
DECL|field|editModifier
specifier|private
name|ChangeEditModifier
name|editModifier
decl_stmt|;
annotation|@
name|Test
DECL|method|getRelatedNoResult ()
specifier|public
name|void
name|getRelatedNoResult
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
decl_stmt|;
name|assertRelated
argument_list|(
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getRelatedLinear ()
specifier|public
name|void
name|getRelatedLinear
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|c1_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 1"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c2_2
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 2"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|c2_2
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|RevCommit
name|c
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|c2_2
argument_list|,
name|c1_1
argument_list|)
control|)
block|{
name|assertRelated
argument_list|(
name|getPatchSetId
argument_list|(
name|c
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id2
argument_list|,
name|c2_2
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id1
argument_list|,
name|c1_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|getRelatedReorder ()
specifier|public
name|void
name|getRelatedReorder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create two commits and push.
name|RevCommit
name|c1_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 1"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c2_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 2"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|ps1_1
init|=
name|getPatchSetId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps2_1
init|=
name|getPatchSetId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
comment|// Swap the order of commits and push again.
name|testRepo
operator|.
name|reset
argument_list|(
literal|"HEAD~2"
argument_list|)
expr_stmt|;
name|RevCommit
name|c2_2
init|=
name|testRepo
operator|.
name|cherryPick
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c1_2
init|=
name|testRepo
operator|.
name|cherryPick
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|ps1_2
init|=
name|getPatchSetId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps2_2
init|=
name|getPatchSetId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
for|for
control|(
name|PatchSet
operator|.
name|Id
name|ps
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|ps2_2
argument_list|,
name|ps1_2
argument_list|)
control|)
block|{
name|assertRelated
argument_list|(
name|ps
argument_list|,
name|changeAndCommit
argument_list|(
name|id1
argument_list|,
name|c1_2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id2
argument_list|,
name|c2_2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatchSet
operator|.
name|Id
name|ps
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|ps2_1
argument_list|,
name|ps1_1
argument_list|)
control|)
block|{
name|assertRelated
argument_list|(
name|ps
argument_list|,
name|changeAndCommit
argument_list|(
name|id2
argument_list|,
name|c2_1
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id1
argument_list|,
name|c1_1
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|getRelatedReorderAndExtend ()
specifier|public
name|void
name|getRelatedReorderAndExtend
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create two commits and push.
name|ObjectId
name|initial
init|=
name|repo
argument_list|()
operator|.
name|getRef
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|RevCommit
name|c1_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 1"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c2_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 2"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|ps1_1
init|=
name|getPatchSetId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps2_1
init|=
name|getPatchSetId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
comment|// Swap the order of commits, create a new commit on top, and push again.
name|testRepo
operator|.
name|reset
argument_list|(
name|initial
argument_list|)
expr_stmt|;
name|RevCommit
name|c2_2
init|=
name|testRepo
operator|.
name|cherryPick
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c1_2
init|=
name|testRepo
operator|.
name|cherryPick
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c3_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"c.txt"
argument_list|,
literal|"3"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 3"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id3
init|=
name|getChangeId
argument_list|(
name|c3_1
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|ps1_2
init|=
name|getPatchSetId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps2_2
init|=
name|getPatchSetId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps3_1
init|=
name|getPatchSetId
argument_list|(
name|c3_1
argument_list|)
decl_stmt|;
for|for
control|(
name|PatchSet
operator|.
name|Id
name|ps
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|ps3_1
argument_list|,
name|ps2_2
argument_list|,
name|ps1_2
argument_list|)
control|)
block|{
name|assertRelated
argument_list|(
name|ps
argument_list|,
name|changeAndCommit
argument_list|(
name|id3
argument_list|,
name|c3_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id1
argument_list|,
name|c1_2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id2
argument_list|,
name|c2_2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatchSet
operator|.
name|Id
name|ps
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|ps2_1
argument_list|,
name|ps1_1
argument_list|)
control|)
block|{
name|assertRelated
argument_list|(
name|ps
argument_list|,
name|changeAndCommit
argument_list|(
name|id3
argument_list|,
name|c3_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id2
argument_list|,
name|c2_1
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id1
argument_list|,
name|c1_1
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|getRelatedEdit ()
specifier|public
name|void
name|getRelatedEdit
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|c1_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 1"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c2_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 2"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
name|RevCommit
name|c3_1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"c.txt"
argument_list|,
literal|"3"
argument_list|)
operator|.
name|message
argument_list|(
literal|"subject: 3"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id3
init|=
name|getChangeId
argument_list|(
name|c3_1
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Change
name|ch2
init|=
name|getChange
argument_list|(
name|c2_1
argument_list|)
operator|.
name|change
argument_list|()
decl_stmt|;
name|editModifier
operator|.
name|createEdit
argument_list|(
name|ch2
argument_list|,
name|getPatchSet
argument_list|(
name|ch2
argument_list|)
argument_list|)
expr_stmt|;
name|editModifier
operator|.
name|modifyFile
argument_list|(
name|editUtil
operator|.
name|byChange
argument_list|(
name|ch2
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
literal|"a.txt"
argument_list|,
name|RestSession
operator|.
name|newRawInput
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|'a'
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectId
name|editRev
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|editUtil
operator|.
name|byChange
argument_list|(
name|ch2
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps1_1
init|=
name|getPatchSetId
argument_list|(
name|c1_1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps2_1
init|=
name|getPatchSetId
argument_list|(
name|c2_1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps2_edit
init|=
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|ch2
operator|.
name|getId
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps3_1
init|=
name|getPatchSetId
argument_list|(
name|c3_1
argument_list|)
decl_stmt|;
for|for
control|(
name|PatchSet
operator|.
name|Id
name|ps
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|ps1_1
argument_list|,
name|ps2_1
argument_list|,
name|ps3_1
argument_list|)
control|)
block|{
name|assertRelated
argument_list|(
name|ps
argument_list|,
name|changeAndCommit
argument_list|(
name|id3
argument_list|,
name|c3_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id2
argument_list|,
name|c2_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id1
argument_list|,
name|c1_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertRelated
argument_list|(
name|ps2_edit
argument_list|,
name|changeAndCommit
argument_list|(
name|id3
argument_list|,
name|c3_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id2
argument_list|,
name|editRev
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|,
name|changeAndCommit
argument_list|(
name|id1
argument_list|,
name|c1_1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getRelated (PatchSet.Id ps)
specifier|private
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|getRelated
parameter_list|(
name|PatchSet
operator|.
name|Id
name|ps
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getRelated
argument_list|(
name|ps
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|ps
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRelated (Change.Id changeId, int ps)
specifier|private
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|getRelated
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|int
name|ps
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|url
init|=
name|String
operator|.
name|format
argument_list|(
literal|"/changes/%d/revisions/%d/related"
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|ps
argument_list|)
decl_stmt|;
return|return
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|adminSession
operator|.
name|get
argument_list|(
name|url
argument_list|)
operator|.
name|getReader
argument_list|()
argument_list|,
name|RelatedInfo
operator|.
name|class
argument_list|)
operator|.
name|changes
return|;
block|}
DECL|method|getChangeId (RevCommit c)
specifier|private
name|String
name|getChangeId
parameter_list|(
name|RevCommit
name|c
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|GitUtil
operator|.
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|c
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getPatchSetId (ObjectId c)
specifier|private
name|PatchSet
operator|.
name|Id
name|getPatchSetId
parameter_list|(
name|ObjectId
name|c
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|getChange
argument_list|(
name|c
argument_list|)
operator|.
name|change
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
return|;
block|}
DECL|method|getPatchSet (Change c)
specifier|private
name|PatchSet
name|getPatchSet
parameter_list|(
name|Change
name|c
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getChange (ObjectId c)
specifier|private
name|ChangeData
name|getChange
parameter_list|(
name|ObjectId
name|c
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byCommit
argument_list|(
name|c
argument_list|)
argument_list|)
return|;
block|}
DECL|method|changeAndCommit (String changeId, ObjectId commitId, int revisionNum, int currentRevisionNum)
specifier|private
specifier|static
name|ChangeAndCommit
name|changeAndCommit
parameter_list|(
name|String
name|changeId
parameter_list|,
name|ObjectId
name|commitId
parameter_list|,
name|int
name|revisionNum
parameter_list|,
name|int
name|currentRevisionNum
parameter_list|)
block|{
name|ChangeAndCommit
name|result
init|=
operator|new
name|ChangeAndCommit
argument_list|()
decl_stmt|;
name|result
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|result
operator|.
name|commit
operator|=
operator|new
name|CommitInfo
argument_list|()
expr_stmt|;
name|result
operator|.
name|commit
operator|.
name|commit
operator|=
name|commitId
operator|.
name|name
argument_list|()
expr_stmt|;
name|result
operator|.
name|_revisionNumber
operator|=
name|revisionNum
expr_stmt|;
name|result
operator|.
name|_currentRevisionNumber
operator|=
name|currentRevisionNum
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|assertRelated (PatchSet.Id psId, ChangeAndCommit... expected)
specifier|private
name|void
name|assertRelated
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|ChangeAndCommit
modifier|...
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|actual
init|=
name|getRelated
argument_list|(
name|psId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actual
argument_list|)
operator|.
name|hasSize
argument_list|(
name|expected
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|actual
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
literal|"index "
operator|+
name|i
operator|+
literal|" related to "
operator|+
name|psId
decl_stmt|;
name|ChangeAndCommit
name|a
init|=
name|actual
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|ChangeAndCommit
name|e
init|=
name|expected
index|[
name|i
index|]
decl_stmt|;
name|assertThat
argument_list|(
name|a
operator|.
name|changeId
argument_list|)
operator|.
name|named
argument_list|(
literal|"Change-Id of "
operator|+
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|e
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|a
operator|.
name|commit
operator|.
name|commit
argument_list|)
operator|.
name|named
argument_list|(
literal|"commit of "
operator|+
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|e
operator|.
name|commit
operator|.
name|commit
argument_list|)
expr_stmt|;
comment|// Don't bother checking _changeNumber; assume changeId is sufficient.
name|assertThat
argument_list|(
name|a
operator|.
name|_revisionNumber
argument_list|)
operator|.
name|named
argument_list|(
literal|"revision of "
operator|+
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|e
operator|.
name|_revisionNumber
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|a
operator|.
name|_currentRevisionNumber
argument_list|)
operator|.
name|named
argument_list|(
literal|"current revision of "
operator|+
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|e
operator|.
name|_currentRevisionNumber
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


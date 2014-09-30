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
name|gerrit
operator|.
name|acceptance
operator|.
name|GitUtil
operator|.
name|add
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
name|createCommit
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
operator|.
name|Commit
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|api
operator|.
name|ResetCommand
operator|.
name|ResetType
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|GitAPIException
throws|,
name|IOException
throws|,
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
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|ps
init|=
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
operator|.
name|getPatchSetId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|related
init|=
name|getRelated
argument_list|(
name|ps
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|related
operator|.
name|size
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
name|GitAPIException
throws|,
name|IOException
throws|,
name|Exception
block|{
name|add
argument_list|(
name|git
argument_list|,
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|Commit
name|c1
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 1"
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|git
argument_list|,
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|Commit
name|c2
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 2"
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|Commit
name|c
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|c2
argument_list|,
name|c1
argument_list|)
control|)
block|{
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|related
init|=
name|getRelated
argument_list|(
name|getPatchSetId
argument_list|(
name|c
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|c
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|c
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|changeId
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
name|GitAPIException
throws|,
name|IOException
throws|,
name|Exception
block|{
comment|// Create two commits and push.
name|add
argument_list|(
name|git
argument_list|,
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|Commit
name|c1
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 1"
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|git
argument_list|,
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|Commit
name|c2
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 2"
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|c1ps1
init|=
name|getPatchSetId
argument_list|(
name|c1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|c2ps1
init|=
name|getPatchSetId
argument_list|(
name|c2
argument_list|)
decl_stmt|;
comment|// Swap the order of commits and push again.
name|git
operator|.
name|reset
argument_list|()
operator|.
name|setMode
argument_list|(
name|ResetType
operator|.
name|HARD
argument_list|)
operator|.
name|setRef
argument_list|(
literal|"HEAD^^"
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|git
operator|.
name|cherryPick
argument_list|()
operator|.
name|include
argument_list|(
name|c2
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|include
argument_list|(
name|c1
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|c1ps2
init|=
name|getPatchSetId
argument_list|(
name|c1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|c2ps2
init|=
name|getPatchSetId
argument_list|(
name|c2
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
name|c2ps2
argument_list|,
name|c1ps2
argument_list|)
control|)
block|{
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|related
init|=
name|getRelated
argument_list|(
name|ps
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|changeId
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
name|c2ps1
argument_list|,
name|c1ps1
argument_list|)
control|)
block|{
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|related
init|=
name|getRelated
argument_list|(
name|ps
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|changeId
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
name|GitAPIException
throws|,
name|IOException
throws|,
name|Exception
block|{
comment|// Create two commits and push.
name|add
argument_list|(
name|git
argument_list|,
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|Commit
name|c1
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 1"
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|git
argument_list|,
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|Commit
name|c2
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 2"
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|c1ps1
init|=
name|getPatchSetId
argument_list|(
name|c1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|c2ps1
init|=
name|getPatchSetId
argument_list|(
name|c2
argument_list|)
decl_stmt|;
comment|// Swap the order of commits, create a new commit on top, and push again.
name|git
operator|.
name|reset
argument_list|()
operator|.
name|setMode
argument_list|(
name|ResetType
operator|.
name|HARD
argument_list|)
operator|.
name|setRef
argument_list|(
literal|"HEAD^^"
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|git
operator|.
name|cherryPick
argument_list|()
operator|.
name|include
argument_list|(
name|c2
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|include
argument_list|(
name|c1
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|git
argument_list|,
literal|"c.txt"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|Commit
name|c3
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 3"
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|c1ps2
init|=
name|getPatchSetId
argument_list|(
name|c1
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|c2ps2
init|=
name|getPatchSetId
argument_list|(
name|c2
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|c3ps1
init|=
name|getPatchSetId
argument_list|(
name|c3
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
name|c3ps1
argument_list|,
name|c2ps2
argument_list|,
name|c1ps2
argument_list|)
control|)
block|{
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|related
init|=
name|getRelated
argument_list|(
name|ps
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c3
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|changeId
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
name|c2ps1
argument_list|,
name|c1ps1
argument_list|)
control|)
block|{
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|related
init|=
name|getRelated
argument_list|(
name|ps
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c3
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|ps
argument_list|,
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|changeId
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
name|add
argument_list|(
name|git
argument_list|,
literal|"a.txt"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|Commit
name|c1
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 1"
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|git
argument_list|,
literal|"b.txt"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|Commit
name|c2
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 2"
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|git
argument_list|,
literal|"b.txt"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|Commit
name|c3
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject: 3"
argument_list|)
decl_stmt|;
name|pushHead
argument_list|(
name|git
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
name|c2
argument_list|)
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
name|String
name|editRev
init|=
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
decl_stmt|;
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|related
init|=
name|getRelated
argument_list|(
name|ch2
operator|.
name|getId
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|c3
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|changeId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"has edit revision number"
argument_list|,
literal|0
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|_revisionNumber
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"has edit revision "
operator|+
name|editRev
argument_list|,
name|editRev
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|commit
operator|.
name|commit
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"related to "
operator|+
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|related
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|changeId
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
DECL|method|getPatchSetId (Commit c)
specifier|private
name|PatchSet
operator|.
name|Id
name|getPatchSetId
parameter_list|(
name|Commit
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
DECL|method|getChange (Commit c)
specifier|private
name|Change
name|getChange
parameter_list|(
name|Commit
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
name|db
operator|.
name|changes
argument_list|()
operator|.
name|byKey
argument_list|(
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|c
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


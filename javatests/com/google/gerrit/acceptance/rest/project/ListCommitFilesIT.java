begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
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
name|getChangeId
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
name|RestResponse
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
name|FileInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
DECL|class|ListCommitFilesIT
specifier|public
class|class
name|ListCommitFilesIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|SUBJECT_1
specifier|private
specifier|static
name|String
name|SUBJECT_1
init|=
literal|"subject 1"
decl_stmt|;
DECL|field|SUBJECT_2
specifier|private
specifier|static
name|String
name|SUBJECT_2
init|=
literal|"subject 2"
decl_stmt|;
DECL|field|FILE_A
specifier|private
specifier|static
name|String
name|FILE_A
init|=
literal|"a.txt"
decl_stmt|;
DECL|field|FILE_B
specifier|private
specifier|static
name|String
name|FILE_B
init|=
literal|"b.txt"
decl_stmt|;
annotation|@
name|Test
DECL|method|listCommitFiles ()
specifier|public
name|void
name|listCommitFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_B
argument_list|,
literal|"2"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_1
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|RevCommit
name|a
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_A
argument_list|,
literal|"1"
argument_list|)
operator|.
name|rm
argument_list|(
name|FILE_B
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|a
argument_list|)
operator|.
name|get
argument_list|()
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
name|RestResponse
name|r
init|=
name|userRestSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/commits/"
operator|+
name|a
operator|.
name|name
argument_list|()
operator|+
literal|"/files/"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|Type
name|type
init|=
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|files1
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|userRestSession
operator|.
name|get
argument_list|(
literal|"/changes/"
operator|+
name|id
operator|+
literal|"/revisions/"
operator|+
name|a
operator|.
name|name
argument_list|()
operator|+
literal|"/files"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|files2
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|files1
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|files2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listMergeCommitFiles ()
specifier|public
name|void
name|listMergeCommitFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createMergeCommitChange
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|RestResponse
name|r
init|=
name|userRestSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/commits/"
operator|+
name|result
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|"/files/?parent=2"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|Type
name|type
init|=
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|files1
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|userRestSession
operator|.
name|get
argument_list|(
literal|"/changes/"
operator|+
name|result
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"/revisions/"
operator|+
name|result
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|"/files/?parent=2"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|files2
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|files1
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|files2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

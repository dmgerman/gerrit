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
DECL|package|com.google.gerrit.server.fixes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|fixes
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
name|server
operator|.
name|edit
operator|.
name|tree
operator|.
name|TreeModificationSubject
operator|.
name|assertThatList
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
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
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|BinaryResult
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
name|Comment
operator|.
name|Range
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
name|FixReplacement
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
name|FileContentUtil
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
name|tree
operator|.
name|TreeModification
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
name|project
operator|.
name|ProjectState
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_class
DECL|class|FixReplacementInterpreterTest
specifier|public
class|class
name|FixReplacementInterpreterTest
block|{
DECL|field|expectedException
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|expectedException
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
DECL|field|fileContentUtil
specifier|private
specifier|final
name|FileContentUtil
name|fileContentUtil
init|=
name|createMock
argument_list|(
name|FileContentUtil
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|repository
specifier|private
specifier|final
name|Repository
name|repository
init|=
name|createMock
argument_list|(
name|Repository
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|projectState
specifier|private
specifier|final
name|ProjectState
name|projectState
init|=
name|createMock
argument_list|(
name|ProjectState
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|patchSetCommitId
specifier|private
specifier|final
name|ObjectId
name|patchSetCommitId
init|=
name|createMock
argument_list|(
name|ObjectId
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|filePath1
specifier|private
specifier|final
name|String
name|filePath1
init|=
literal|"an/arbitrary/file.txt"
decl_stmt|;
DECL|field|filePath2
specifier|private
specifier|final
name|String
name|filePath2
init|=
literal|"another/arbitrary/file.txt"
decl_stmt|;
DECL|field|fixReplacementInterpreter
specifier|private
name|FixReplacementInterpreter
name|fixReplacementInterpreter
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|fixReplacementInterpreter
operator|=
operator|new
name|FixReplacementInterpreter
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noReplacementsResultInNoTreeModifications ()
specifier|public
name|void
name|noReplacementsResultInNoTreeModifications
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|()
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|treeModificationsTargetCorrectFiles ()
specifier|public
name|void
name|treeModificationsTargetCorrectFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|1
argument_list|,
literal|6
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|)
argument_list|,
literal|"Modified content"
argument_list|)
decl_stmt|;
name|FixReplacement
name|fixReplacement2
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|,
literal|"Second modification"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|FixReplacement
name|fixReplacement3
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath2
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"Another modified content"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath2
argument_list|,
literal|"1st line\n2nd line\n3rd line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|,
name|fixReplacement3
argument_list|,
name|fixReplacement2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|sortedTreeModifications
init|=
name|getSortedCopy
argument_list|(
name|treeModifications
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|sortedTreeModifications
argument_list|)
operator|.
name|element
argument_list|(
literal|0
argument_list|)
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|filePath
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|filePath1
argument_list|)
expr_stmt|;
name|assertThatList
argument_list|(
name|sortedTreeModifications
argument_list|)
operator|.
name|element
argument_list|(
literal|0
argument_list|)
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"First"
argument_list|)
expr_stmt|;
name|assertThatList
argument_list|(
name|sortedTreeModifications
argument_list|)
operator|.
name|element
argument_list|(
literal|1
argument_list|)
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|filePath
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|filePath2
argument_list|)
expr_stmt|;
name|assertThatList
argument_list|(
name|sortedTreeModifications
argument_list|)
operator|.
name|element
argument_list|(
literal|1
argument_list|)
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"1st"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsCanDeleteALine ()
specifier|public
name|void
name|replacementsCanDeleteALine
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"First line\nThird line\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsCanAddALine ()
specifier|public
name|void
name|replacementsCanAddALine
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"A new line\n"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"First line\nA new line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMaySpanMultipleLines ()
specifier|public
name|void
name|replacementsMaySpanMultipleLines
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|1
argument_list|,
literal|6
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|,
literal|"and t"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"First and third line\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMayOccurOnSameLine ()
specifier|public
name|void
name|replacementsMayOccurOnSameLine
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement1
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|,
literal|6
argument_list|)
argument_list|,
literal|"A"
argument_list|)
decl_stmt|;
name|FixReplacement
name|fixReplacement2
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|7
argument_list|,
literal|2
argument_list|,
literal|11
argument_list|)
argument_list|,
literal|"modification"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement1
argument_list|,
name|fixReplacement2
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"First line\nA modification\nThird line\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMayTouch ()
specifier|public
name|void
name|replacementsMayTouch
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement1
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|1
argument_list|,
literal|6
argument_list|,
literal|2
argument_list|,
literal|7
argument_list|)
argument_list|,
literal|"modified "
argument_list|)
decl_stmt|;
name|FixReplacement
name|fixReplacement2
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|7
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|,
literal|"content"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement1
argument_list|,
name|fixReplacement2
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"First modified content line\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsCanAddContentAtEndOfFile ()
specifier|public
name|void
name|replacementsCanAddContentAtEndOfFile
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|4
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"New content"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"First line\nSecond line\nThird line\nNew content"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsCanModifySeveralFilesInAnyOrder ()
specifier|public
name|void
name|replacementsCanModifySeveralFilesInAnyOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement1
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|)
argument_list|,
literal|"Modified content"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|FixReplacement
name|fixReplacement2
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath2
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"First modification\n"
argument_list|)
decl_stmt|;
name|FixReplacement
name|fixReplacement3
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath2
argument_list|,
operator|new
name|Range
argument_list|(
literal|3
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"Second modification\n"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath2
argument_list|,
literal|"1st line\n2nd line\n3rd line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement3
argument_list|,
name|fixReplacement1
argument_list|,
name|fixReplacement2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|sortedTreeModifications
init|=
name|getSortedCopy
argument_list|(
name|treeModifications
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|sortedTreeModifications
argument_list|)
operator|.
name|element
argument_list|(
literal|0
argument_list|)
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"FModified contentird line\n"
argument_list|)
expr_stmt|;
name|assertThatList
argument_list|(
name|sortedTreeModifications
argument_list|)
operator|.
name|element
argument_list|(
literal|1
argument_list|)
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"1st line\nFirst modification\nSecond modification\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lineSeparatorCanBeChanged ()
specifier|public
name|void
name|lineSeparatorCanBeChanged
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|2
argument_list|,
literal|11
argument_list|,
literal|3
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"\r"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"First line\nSecond line\rThird line\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsDoNotNeedToBeOrderedAccordingToRange ()
specifier|public
name|void
name|replacementsDoNotNeedToBeOrderedAccordingToRange
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement1
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"1st modification\n"
argument_list|)
decl_stmt|;
name|FixReplacement
name|fixReplacement2
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|3
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"2nd modification\n"
argument_list|)
decl_stmt|;
name|FixReplacement
name|fixReplacement3
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|4
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"3rd modification\n"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\nFourth line\nFifth line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|toTreeModifications
argument_list|(
name|fixReplacement2
argument_list|,
name|fixReplacement1
argument_list|,
name|fixReplacement3
argument_list|)
decl_stmt|;
name|assertThatList
argument_list|(
name|treeModifications
argument_list|)
operator|.
name|onlyElement
argument_list|()
operator|.
name|asChangeFileContentModification
argument_list|()
operator|.
name|newContent
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"1st modification\nSecond line\n2nd modification\n3rd modification\nFifth line\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMustNotReferToNotExistingLine ()
specifier|public
name|void
name|replacementsMustNotReferToNotExistingLine
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|5
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"A new line\n"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMustNotReferToZeroLine ()
specifier|public
name|void
name|replacementsMustNotReferToZeroLine
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"A new line\n"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMustNotReferToNotExistingOffsetOfIntermediateLine ()
specifier|public
name|void
name|replacementsMustNotReferToNotExistingOffsetOfIntermediateLine
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|11
argument_list|)
argument_list|,
literal|"modified"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMustNotReferToNotExistingOffsetOfLastLine ()
specifier|public
name|void
name|replacementsMustNotReferToNotExistingOffsetOfLastLine
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|3
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|,
literal|11
argument_list|)
argument_list|,
literal|"modified"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replacementsMustNotReferToNegativeOffset ()
specifier|public
name|void
name|replacementsMustNotReferToNegativeOffset
parameter_list|()
throws|throws
name|Exception
block|{
name|FixReplacement
name|fixReplacement
init|=
operator|new
name|FixReplacement
argument_list|(
name|filePath1
argument_list|,
operator|new
name|Range
argument_list|(
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|,
literal|"modified"
argument_list|)
decl_stmt|;
name|mockFileContent
argument_list|(
name|filePath1
argument_list|,
literal|"First line\nSecond line\nThird line\n"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|fileContentUtil
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|toTreeModifications
argument_list|(
name|fixReplacement
argument_list|)
expr_stmt|;
block|}
DECL|method|mockFileContent (String filePath, String fileContent)
specifier|private
name|void
name|mockFileContent
parameter_list|(
name|String
name|filePath
parameter_list|,
name|String
name|fileContent
parameter_list|)
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|fileContentUtil
operator|.
name|getContent
argument_list|(
name|repository
argument_list|,
name|projectState
argument_list|,
name|patchSetCommitId
argument_list|,
name|filePath
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|BinaryResult
operator|.
name|create
argument_list|(
name|fileContent
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|toTreeModifications (FixReplacement... fixReplacements)
specifier|private
name|List
argument_list|<
name|TreeModification
argument_list|>
name|toTreeModifications
parameter_list|(
name|FixReplacement
modifier|...
name|fixReplacements
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|fixReplacementInterpreter
operator|.
name|toTreeModifications
argument_list|(
name|repository
argument_list|,
name|projectState
argument_list|,
name|patchSetCommitId
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|fixReplacements
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getSortedCopy (List<TreeModification> treeModifications)
specifier|private
specifier|static
name|List
argument_list|<
name|TreeModification
argument_list|>
name|getSortedCopy
parameter_list|(
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
parameter_list|)
block|{
name|List
argument_list|<
name|TreeModification
argument_list|>
name|sortedTreeModifications
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|treeModifications
argument_list|)
decl_stmt|;
name|sortedTreeModifications
operator|.
name|sort
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|TreeModification
operator|::
name|getFilePath
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sortedTreeModifications
return|;
block|}
block|}
end_class

end_unit


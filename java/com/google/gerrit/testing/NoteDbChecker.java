begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|assertThat
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|base
operator|.
name|Joiner
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
name|ImmutableListMultimap
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
name|ListMultimap
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
name|flogger
operator|.
name|FluentLogger
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
name|Project
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|server
operator|.
name|ReviewDbUtil
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
name|CommentsUtil
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
name|git
operator|.
name|GitRepositoryManager
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
name|notedb
operator|.
name|ChangeBundle
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
name|notedb
operator|.
name|ChangeBundleReader
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
name|notedb
operator|.
name|ChangeNotes
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
name|notedb
operator|.
name|MutableNotesMigration
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
name|notedb
operator|.
name|rebuild
operator|.
name|ChangeRebuilder
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
name|client
operator|.
name|IntKey
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmRuntimeException
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
name|Provider
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
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|runner
operator|.
name|Description
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|NoteDbChecker
specifier|public
class|class
name|NoteDbChecker
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|notesMigration
specifier|private
specifier|final
name|MutableNotesMigration
name|notesMigration
decl_stmt|;
DECL|field|bundleReader
specifier|private
specifier|final
name|ChangeBundleReader
name|bundleReader
decl_stmt|;
DECL|field|notesFactory
specifier|private
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
decl_stmt|;
DECL|field|changeRebuilder
specifier|private
specifier|final
name|ChangeRebuilder
name|changeRebuilder
decl_stmt|;
DECL|field|commentsUtil
specifier|private
specifier|final
name|CommentsUtil
name|commentsUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|NoteDbChecker ( Provider<ReviewDb> dbProvider, GitRepositoryManager repoManager, MutableNotesMigration notesMigration, ChangeBundleReader bundleReader, ChangeNotes.Factory notesFactory, ChangeRebuilder changeRebuilder, CommentsUtil commentsUtil)
name|NoteDbChecker
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|MutableNotesMigration
name|notesMigration
parameter_list|,
name|ChangeBundleReader
name|bundleReader
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
parameter_list|,
name|ChangeRebuilder
name|changeRebuilder
parameter_list|,
name|CommentsUtil
name|commentsUtil
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|bundleReader
operator|=
name|bundleReader
expr_stmt|;
name|this
operator|.
name|notesMigration
operator|=
name|notesMigration
expr_stmt|;
name|this
operator|.
name|notesFactory
operator|=
name|notesFactory
expr_stmt|;
name|this
operator|.
name|changeRebuilder
operator|=
name|changeRebuilder
expr_stmt|;
name|this
operator|.
name|commentsUtil
operator|=
name|commentsUtil
expr_stmt|;
block|}
DECL|method|rebuildAndCheckAllChanges ()
specifier|public
name|void
name|rebuildAndCheckAllChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|rebuildAndCheckChanges
argument_list|(
name|getUnwrappedDb
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Change
operator|::
name|getId
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|rebuildAndCheckChanges (Change.Id... changeIds)
specifier|public
name|void
name|rebuildAndCheckChanges
parameter_list|(
name|Change
operator|.
name|Id
modifier|...
name|changeIds
parameter_list|)
throws|throws
name|Exception
block|{
name|rebuildAndCheckChanges
argument_list|(
name|Arrays
operator|.
name|stream
argument_list|(
name|changeIds
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|rebuildAndCheckChanges ( Stream<Change.Id> changeIds, ListMultimap<Change.Id, String> expectedDiffs)
specifier|private
name|void
name|rebuildAndCheckChanges
parameter_list|(
name|Stream
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|changeIds
parameter_list|,
name|ListMultimap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|String
argument_list|>
name|expectedDiffs
parameter_list|)
throws|throws
name|Exception
block|{
name|ReviewDb
name|db
init|=
name|getUnwrappedDb
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ChangeBundle
argument_list|>
name|allExpected
init|=
name|readExpected
argument_list|(
name|changeIds
argument_list|)
decl_stmt|;
name|boolean
name|oldWrite
init|=
name|notesMigration
operator|.
name|rawWriteChangesSetting
argument_list|()
decl_stmt|;
name|boolean
name|oldRead
init|=
name|notesMigration
operator|.
name|readChanges
argument_list|()
decl_stmt|;
try|try
block|{
name|notesMigration
operator|.
name|setWriteChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|notesMigration
operator|.
name|setReadChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|msgs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeBundle
name|expected
range|:
name|allExpected
control|)
block|{
name|Change
name|c
init|=
name|expected
operator|.
name|getChange
argument_list|()
decl_stmt|;
try|try
block|{
name|changeRebuilder
operator|.
name|rebuild
argument_list|(
name|db
argument_list|,
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|msgs
operator|.
name|add
argument_list|(
literal|"Repository not found for change, cannot convert: "
operator|+
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|checkActual
argument_list|(
name|allExpected
argument_list|,
name|expectedDiffs
argument_list|,
name|msgs
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|notesMigration
operator|.
name|setReadChanges
argument_list|(
name|oldRead
argument_list|)
expr_stmt|;
name|notesMigration
operator|.
name|setWriteChanges
argument_list|(
name|oldWrite
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkChanges (Change.Id... changeIds)
specifier|public
name|void
name|checkChanges
parameter_list|(
name|Change
operator|.
name|Id
modifier|...
name|changeIds
parameter_list|)
throws|throws
name|Exception
block|{
name|checkActual
argument_list|(
name|readExpected
argument_list|(
name|Arrays
operator|.
name|stream
argument_list|(
name|changeIds
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|rebuildAndCheckChange (Change.Id changeId, String... expectedDiff)
specifier|public
name|void
name|rebuildAndCheckChange
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
modifier|...
name|expectedDiff
parameter_list|)
throws|throws
name|Exception
block|{
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|String
argument_list|>
name|b
init|=
name|ImmutableListMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|b
operator|.
name|putAll
argument_list|(
name|changeId
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|expectedDiff
argument_list|)
argument_list|)
expr_stmt|;
name|rebuildAndCheckChanges
argument_list|(
name|Stream
operator|.
name|of
argument_list|(
name|changeId
argument_list|)
argument_list|,
name|b
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNoChangeRef (Project.NameKey project, Change.Id changeId)
specifier|public
name|void
name|assertNoChangeRef
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|assertNoReviewDbChanges (Description desc)
specifier|public
name|void
name|assertNoReviewDbChanges
parameter_list|(
name|Description
name|desc
parameter_list|)
throws|throws
name|Exception
block|{
name|ReviewDb
name|db
init|=
name|getUnwrappedDb
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"Changes in "
operator|+
name|desc
operator|.
name|getTestClass
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"ChangeMessages in "
operator|+
name|desc
operator|.
name|getTestClass
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"PatchSets in "
operator|+
name|desc
operator|.
name|getTestClass
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"PatchSetApprovals in "
operator|+
name|desc
operator|.
name|getTestClass
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"PatchLineComments in "
operator|+
name|desc
operator|.
name|getTestClass
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|readExpected (Stream<Change.Id> changeIds)
specifier|private
name|List
argument_list|<
name|ChangeBundle
argument_list|>
name|readExpected
parameter_list|(
name|Stream
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|changeIds
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|old
init|=
name|notesMigration
operator|.
name|readChanges
argument_list|()
decl_stmt|;
try|try
block|{
name|notesMigration
operator|.
name|setReadChanges
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|changeIds
operator|.
name|sorted
argument_list|(
name|comparing
argument_list|(
name|IntKey
operator|::
name|get
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|this
operator|::
name|readBundleUnchecked
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|notesMigration
operator|.
name|setReadChanges
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|readBundleUnchecked (Change.Id id)
specifier|private
name|ChangeBundle
name|readBundleUnchecked
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
try|try
block|{
return|return
name|bundleReader
operator|.
name|fromReviewDb
argument_list|(
name|getUnwrappedDb
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|checkActual ( List<ChangeBundle> allExpected, ListMultimap<Change.Id, String> expectedDiffs, List<String> msgs)
specifier|private
name|void
name|checkActual
parameter_list|(
name|List
argument_list|<
name|ChangeBundle
argument_list|>
name|allExpected
parameter_list|,
name|ListMultimap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|String
argument_list|>
name|expectedDiffs
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|msgs
parameter_list|)
throws|throws
name|Exception
block|{
name|ReviewDb
name|db
init|=
name|getUnwrappedDb
argument_list|()
decl_stmt|;
name|boolean
name|oldRead
init|=
name|notesMigration
operator|.
name|readChanges
argument_list|()
decl_stmt|;
name|boolean
name|oldWrite
init|=
name|notesMigration
operator|.
name|rawWriteChangesSetting
argument_list|()
decl_stmt|;
try|try
block|{
name|notesMigration
operator|.
name|setWriteChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|notesMigration
operator|.
name|setReadChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|ChangeBundle
name|expected
range|:
name|allExpected
control|)
block|{
name|Change
name|c
init|=
name|expected
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|ChangeBundle
name|actual
decl_stmt|;
try|try
block|{
name|actual
operator|=
name|ChangeBundle
operator|.
name|fromNotes
argument_list|(
name|commentsUtil
argument_list|,
name|notesFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|c
operator|.
name|getProject
argument_list|()
argument_list|,
name|c
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error converting change: "
operator|+
name|c
decl_stmt|;
name|msgs
operator|.
name|add
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|t
argument_list|)
operator|.
name|log
argument_list|(
name|msg
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|diff
init|=
name|expected
operator|.
name|differencesFrom
argument_list|(
name|actual
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expectedDiff
init|=
name|expectedDiffs
operator|.
name|get
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|diff
operator|.
name|equals
argument_list|(
name|expectedDiff
argument_list|)
condition|)
block|{
name|msgs
operator|.
name|add
argument_list|(
literal|"Differences between ReviewDb and NoteDb for "
operator|+
name|c
operator|+
literal|":"
argument_list|)
expr_stmt|;
name|msgs
operator|.
name|addAll
argument_list|(
name|diff
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|expectedDiff
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|msgs
operator|.
name|add
argument_list|(
literal|"Expected differences:"
argument_list|)
expr_stmt|;
name|msgs
operator|.
name|addAll
argument_list|(
name|expectedDiff
argument_list|)
expr_stmt|;
block|}
name|msgs
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"NoteDb conversion of change "
operator|+
name|c
operator|.
name|getId
argument_list|()
operator|+
literal|" successful"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|notesMigration
operator|.
name|setReadChanges
argument_list|(
name|oldRead
argument_list|)
expr_stmt|;
name|notesMigration
operator|.
name|setWriteChanges
argument_list|(
name|oldWrite
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|msgs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|join
argument_list|(
name|msgs
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|getUnwrappedDb ()
specifier|private
name|ReviewDb
name|getUnwrappedDb
parameter_list|()
block|{
name|ReviewDb
name|db
init|=
name|dbProvider
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|ReviewDbUtil
operator|.
name|unwrapDb
argument_list|(
name|db
argument_list|)
return|;
block|}
block|}
end_class

end_unit


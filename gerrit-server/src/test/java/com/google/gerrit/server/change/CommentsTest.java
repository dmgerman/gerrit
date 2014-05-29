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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
package|;
end_package

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
name|expect
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
name|expectLastCall
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|fail
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
name|Objects
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
name|ImmutableMap
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
name|Lists
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
name|common
operator|.
name|changes
operator|.
name|Side
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
name|registration
operator|.
name|DynamicMap
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
name|IdString
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
name|ResourceNotFoundException
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
name|RestReadView
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
name|RestView
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
name|Account
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
name|CommentRange
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
name|Patch
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
name|PatchLineComment
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
name|PatchLineComment
operator|.
name|Status
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
name|reviewdb
operator|.
name|server
operator|.
name|PatchLineCommentAccess
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
name|server
operator|.
name|account
operator|.
name|AccountInfo
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
name|util
operator|.
name|TimeUtil
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
name|ListResultSet
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
name|ResultSet
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
name|AbstractModule
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
name|Guice
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
name|Injector
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
name|TypeLiteral
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IAnswer
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
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|Collections
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
name|Map
import|;
end_import

begin_class
DECL|class|CommentsTest
specifier|public
class|class
name|CommentsTest
block|{
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
DECL|field|revRes1
specifier|private
name|RevisionResource
name|revRes1
decl_stmt|;
DECL|field|revRes2
specifier|private
name|RevisionResource
name|revRes2
decl_stmt|;
DECL|field|plc1
specifier|private
name|PatchLineComment
name|plc1
decl_stmt|;
DECL|field|plc2
specifier|private
name|PatchLineComment
name|plc2
decl_stmt|;
DECL|field|plc3
specifier|private
name|PatchLineComment
name|plc3
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
name|views
init|=
name|createMock
argument_list|(
name|DynamicMap
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|TypeLiteral
argument_list|<
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
argument_list|>
name|viewsType
init|=
operator|new
name|TypeLiteral
argument_list|<
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
specifier|final
name|AccountInfo
operator|.
name|Loader
operator|.
name|Factory
name|alf
init|=
name|createMock
argument_list|(
name|AccountInfo
operator|.
name|Loader
operator|.
name|Factory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ReviewDb
name|db
init|=
name|createMock
argument_list|(
name|ReviewDb
operator|.
name|class
argument_list|)
decl_stmt|;
name|AbstractModule
name|mod
init|=
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|viewsType
argument_list|)
operator|.
name|toInstance
argument_list|(
name|views
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AccountInfo
operator|.
name|Loader
operator|.
name|Factory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|alf
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ReviewDb
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Account
operator|.
name|Id
name|account1
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Account
operator|.
name|Id
name|account2
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|AccountInfo
operator|.
name|Loader
name|accountLoader
init|=
name|createMock
argument_list|(
name|AccountInfo
operator|.
name|Loader
operator|.
name|class
argument_list|)
decl_stmt|;
name|accountLoader
operator|.
name|fill
argument_list|()
expr_stmt|;
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|accountLoader
operator|.
name|get
argument_list|(
name|account1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|AccountInfo
argument_list|(
name|account1
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|accountLoader
operator|.
name|get
argument_list|(
name|account2
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|AccountInfo
argument_list|(
name|account2
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|alf
operator|.
name|create
argument_list|(
literal|true
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|accountLoader
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|accountLoader
argument_list|,
name|alf
argument_list|)
expr_stmt|;
name|revRes1
operator|=
name|createMock
argument_list|(
name|RevisionResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|revRes2
operator|=
name|createMock
argument_list|(
name|RevisionResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|PatchLineCommentAccess
name|plca
init|=
name|createMock
argument_list|(
name|PatchLineCommentAccess
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|plca
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|Change
operator|.
name|Id
name|changeId
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|123
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|psId1
init|=
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|PatchSet
name|ps1
init|=
operator|new
name|PatchSet
argument_list|(
name|psId1
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|revRes1
operator|.
name|getPatchSet
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ps1
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|psId2
init|=
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|PatchSet
name|ps2
init|=
operator|new
name|PatchSet
argument_list|(
name|psId2
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|revRes2
operator|.
name|getPatchSet
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ps2
argument_list|)
expr_stmt|;
name|long
name|timeBase
init|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
decl_stmt|;
name|plc1
operator|=
name|newPatchLineComment
argument_list|(
name|psId1
argument_list|,
literal|"Comment1"
argument_list|,
literal|null
argument_list|,
literal|"FileOne.txt"
argument_list|,
name|Side
operator|.
name|REVISION
argument_list|,
literal|1
argument_list|,
name|account1
argument_list|,
name|timeBase
argument_list|,
literal|"First Comment"
argument_list|,
operator|new
name|CommentRange
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|plc2
operator|=
name|newPatchLineComment
argument_list|(
name|psId1
argument_list|,
literal|"Comment2"
argument_list|,
literal|"Comment1"
argument_list|,
literal|"FileOne.txt"
argument_list|,
name|Side
operator|.
name|REVISION
argument_list|,
literal|1
argument_list|,
name|account2
argument_list|,
name|timeBase
operator|+
literal|1000
argument_list|,
literal|"Reply to First Comment"
argument_list|,
operator|new
name|CommentRange
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|plc3
operator|=
name|newPatchLineComment
argument_list|(
name|psId1
argument_list|,
literal|"Comment3"
argument_list|,
literal|"Comment1"
argument_list|,
literal|"FileOne.txt"
argument_list|,
name|Side
operator|.
name|PARENT
argument_list|,
literal|1
argument_list|,
name|account1
argument_list|,
name|timeBase
operator|+
literal|2000
argument_list|,
literal|"First Parent Comment"
argument_list|,
operator|new
name|CommentRange
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|plca
operator|.
name|publishedByPatchSet
argument_list|(
name|psId1
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
name|results
argument_list|(
name|plc1
argument_list|,
name|plc2
argument_list|,
name|plc3
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|plca
operator|.
name|publishedByPatchSet
argument_list|(
name|psId2
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
name|results
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|db
argument_list|,
name|revRes1
argument_list|,
name|revRes2
argument_list|,
name|plca
argument_list|)
expr_stmt|;
name|injector
operator|=
name|Guice
operator|.
name|createInjector
argument_list|(
name|mod
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testListComments ()
specifier|public
name|void
name|testListComments
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test ListComments for patch set 1
name|assertListComments
argument_list|(
name|injector
argument_list|,
name|revRes1
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"FileOne.txt"
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|plc3
argument_list|,
name|plc1
argument_list|,
name|plc2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// test ListComments for patch set 2
name|assertListComments
argument_list|(
name|injector
argument_list|,
name|revRes2
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
operator|>
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetComment ()
specifier|public
name|void
name|testGetComment
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test GetComment for existing comment
name|assertGetComment
argument_list|(
name|injector
argument_list|,
name|revRes1
argument_list|,
name|plc1
argument_list|,
name|plc1
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// test GetComment for non-existent comment
name|assertGetComment
argument_list|(
name|injector
argument_list|,
name|revRes1
argument_list|,
literal|null
argument_list|,
literal|"BadComment"
argument_list|)
expr_stmt|;
block|}
DECL|method|results ( final PatchLineComment... comments)
specifier|private
specifier|static
name|IAnswer
argument_list|<
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|>
name|results
parameter_list|(
specifier|final
name|PatchLineComment
modifier|...
name|comments
parameter_list|)
block|{
return|return
operator|new
name|IAnswer
argument_list|<
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|answer
parameter_list|()
throws|throws
name|Throwable
block|{
return|return
operator|new
name|ListResultSet
argument_list|<>
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|comments
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|assertGetComment (Injector inj, RevisionResource res, PatchLineComment expected, String uuid)
specifier|private
specifier|static
name|void
name|assertGetComment
parameter_list|(
name|Injector
name|inj
parameter_list|,
name|RevisionResource
name|res
parameter_list|,
name|PatchLineComment
name|expected
parameter_list|,
name|String
name|uuid
parameter_list|)
throws|throws
name|Exception
block|{
name|GetComment
name|getComment
init|=
name|inj
operator|.
name|getInstance
argument_list|(
name|GetComment
operator|.
name|class
argument_list|)
decl_stmt|;
name|Comments
name|comments
init|=
name|inj
operator|.
name|getInstance
argument_list|(
name|Comments
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|CommentResource
name|commentRes
init|=
name|comments
operator|.
name|parse
argument_list|(
name|res
argument_list|,
name|IdString
operator|.
name|fromUrl
argument_list|(
name|uuid
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|expected
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Expected no comment"
argument_list|)
expr_stmt|;
block|}
name|CommentInfo
name|actual
init|=
name|getComment
operator|.
name|apply
argument_list|(
name|commentRes
argument_list|)
decl_stmt|;
name|assertComment
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
if|if
condition|(
name|expected
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Expected to find comment"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|assertListComments (Injector inj, RevisionResource res, Map<String, ArrayList<PatchLineComment>> expected)
specifier|private
specifier|static
name|void
name|assertListComments
parameter_list|(
name|Injector
name|inj
parameter_list|,
name|RevisionResource
name|res
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|>
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|Comments
name|comments
init|=
name|inj
operator|.
name|getInstance
argument_list|(
name|Comments
operator|.
name|class
argument_list|)
decl_stmt|;
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
name|listView
init|=
operator|(
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
operator|)
name|comments
operator|.
name|list
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|actual
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
operator|)
name|listView
operator|.
name|apply
argument_list|(
name|res
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|actual
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|size
argument_list|()
argument_list|,
name|actual
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|keySet
argument_list|()
argument_list|,
name|actual
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|>
name|entry
range|:
name|expected
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|expectedComments
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CommentInfo
argument_list|>
name|actualComments
init|=
name|actual
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|actualComments
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedComments
operator|.
name|size
argument_list|()
argument_list|,
name|actualComments
operator|.
name|size
argument_list|()
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
name|expectedComments
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|assertComment
argument_list|(
name|expectedComments
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|actualComments
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|assertComment (PatchLineComment plc, CommentInfo ci)
specifier|private
specifier|static
name|void
name|assertComment
parameter_list|(
name|PatchLineComment
name|plc
parameter_list|,
name|CommentInfo
name|ci
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|plc
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|ci
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|plc
operator|.
name|getParentUuid
argument_list|()
argument_list|,
name|ci
operator|.
name|inReplyTo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|plc
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ci
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ci
operator|.
name|author
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|plc
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|ci
operator|.
name|author
operator|.
name|_id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|plc
operator|.
name|getLine
argument_list|()
argument_list|,
operator|(
name|int
operator|)
name|ci
operator|.
name|line
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|plc
operator|.
name|getSide
argument_list|()
operator|==
literal|0
condition|?
name|Side
operator|.
name|PARENT
else|:
name|Side
operator|.
name|REVISION
argument_list|,
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|ci
operator|.
name|side
argument_list|,
name|Side
operator|.
name|REVISION
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|plc
operator|.
name|getWrittenOn
argument_list|()
argument_list|,
name|ci
operator|.
name|updated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|plc
operator|.
name|getRange
argument_list|()
argument_list|,
name|ci
operator|.
name|range
argument_list|)
expr_stmt|;
block|}
DECL|method|newPatchLineComment (PatchSet.Id psId, String uuid, String inReplyToUuid, String filename, Side side, int line, Account.Id authorId, long millis, String message, CommentRange range)
specifier|private
specifier|static
name|PatchLineComment
name|newPatchLineComment
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|String
name|uuid
parameter_list|,
name|String
name|inReplyToUuid
parameter_list|,
name|String
name|filename
parameter_list|,
name|Side
name|side
parameter_list|,
name|int
name|line
parameter_list|,
name|Account
operator|.
name|Id
name|authorId
parameter_list|,
name|long
name|millis
parameter_list|,
name|String
name|message
parameter_list|,
name|CommentRange
name|range
parameter_list|)
block|{
name|Patch
operator|.
name|Key
name|p
init|=
operator|new
name|Patch
operator|.
name|Key
argument_list|(
name|psId
argument_list|,
name|filename
argument_list|)
decl_stmt|;
name|PatchLineComment
operator|.
name|Key
name|id
init|=
operator|new
name|PatchLineComment
operator|.
name|Key
argument_list|(
name|p
argument_list|,
name|uuid
argument_list|)
decl_stmt|;
name|PatchLineComment
name|plc
init|=
operator|new
name|PatchLineComment
argument_list|(
name|id
argument_list|,
name|line
argument_list|,
name|authorId
argument_list|,
name|inReplyToUuid
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|plc
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setRange
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setSide
argument_list|(
name|side
operator|==
name|Side
operator|.
name|PARENT
condition|?
operator|(
name|short
operator|)
literal|0
else|:
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setStatus
argument_list|(
name|Status
operator|.
name|PUBLISHED
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setWrittenOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|millis
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|plc
return|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|NoHttpd
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
name|api
operator|.
name|changes
operator|.
name|ReviewInput
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|junit
operator|.
name|TestRepository
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
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|RefSpec
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
annotation|@
name|NoHttpd
DECL|class|SubmoduleSubscriptionsWholeTopicMergeIT
specifier|public
class|class
name|SubmoduleSubscriptionsWholeTopicMergeIT
extends|extends
name|AbstractSubmoduleSubscription
block|{
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|submitWholeTopicEnabled ()
specifier|public
specifier|static
name|Config
name|submitWholeTopicEnabled
parameter_list|()
block|{
return|return
name|submitWholeTopicEnabledConfig
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testSubscriptionUpdateOfManyChanges ()
specifier|public
name|void
name|testSubscriptionUpdateOfManyChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|superRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"super-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|subRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"subscribed-to-project"
argument_list|)
decl_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"subscribed-to-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"super-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"subscribed-to-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|ObjectId
name|subHEAD
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|RevCommit
name|c
init|=
name|subRepo
operator|.
name|getRevWalk
argument_list|()
operator|.
name|parseCommit
argument_list|(
name|subHEAD
argument_list|)
decl_stmt|;
name|RevCommit
name|c1
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first change"
argument_list|)
operator|.
name|add
argument_list|(
literal|"asdf"
argument_list|,
literal|"asdf\n"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/for/master/"
operator|+
name|name
argument_list|(
literal|"topic-foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|subRepo
operator|.
name|reset
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|c2
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"qwerty"
argument_list|)
operator|.
name|add
argument_list|(
literal|"qwerty"
argument_list|,
literal|"qwerty"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|RevCommit
name|c3
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"qwerty followup"
argument_list|)
operator|.
name|add
argument_list|(
literal|"qwerty"
argument_list|,
literal|"qwerty\nqwerty\n"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/for/master/"
operator|+
name|name
argument_list|(
literal|"topic-foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|c1
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|c2
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|id3
init|=
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|c3
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id2
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id3
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|ObjectId
name|subRepoId
init|=
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|fetch
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|call
argument_list|()
operator|.
name|getAdvertisedRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|expectToHaveSubmoduleState
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"subscribed-to-project"
argument_list|,
name|subRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubscriptionUpdateIncludingChangeInSuperproject ()
specifier|public
name|void
name|testSubscriptionUpdateIncludingChangeInSuperproject
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|superRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"super-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|subRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"subscribed-to-project"
argument_list|)
decl_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"subscribed-to-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"super-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"subscribed-to-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|ObjectId
name|subHEAD
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|RevCommit
name|c
init|=
name|subRepo
operator|.
name|getRevWalk
argument_list|()
operator|.
name|parseCommit
argument_list|(
name|subHEAD
argument_list|)
decl_stmt|;
name|RevCommit
name|c1
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first change"
argument_list|)
operator|.
name|add
argument_list|(
literal|"asdf"
argument_list|,
literal|"asdf\n"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/for/master/"
operator|+
name|name
argument_list|(
literal|"topic-foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|subRepo
operator|.
name|reset
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|c2
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"qwerty"
argument_list|)
operator|.
name|add
argument_list|(
literal|"qwerty"
argument_list|,
literal|"qwerty"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|RevCommit
name|c3
init|=
name|subRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"qwerty followup"
argument_list|)
operator|.
name|add
argument_list|(
literal|"qwerty"
argument_list|,
literal|"qwerty\nqwerty\n"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/for/master/"
operator|+
name|name
argument_list|(
literal|"topic-foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|RevCommit
name|c4
init|=
name|superRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"new change on superproject"
argument_list|)
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|superRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/for/master/"
operator|+
name|name
argument_list|(
literal|"topic-foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|c1
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|c2
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|id3
init|=
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|c3
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|id4
init|=
name|getChangeId
argument_list|(
name|superRepo
argument_list|,
name|c4
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id2
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id3
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id4
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|ObjectId
name|subRepoId
init|=
name|subRepo
operator|.
name|git
argument_list|()
operator|.
name|fetch
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|call
argument_list|()
operator|.
name|getAdvertisedRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|expectToHaveSubmoduleState
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"subscribed-to-project"
argument_list|,
name|subRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateManySubmodules ()
specifier|public
name|void
name|testUpdateManySubmodules
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|superRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"super-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|sub1
init|=
name|createProjectWithPush
argument_list|(
literal|"sub1"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|sub2
init|=
name|createProjectWithPush
argument_list|(
literal|"sub2"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|sub3
init|=
name|createProjectWithPush
argument_list|(
literal|"sub3"
argument_list|)
decl_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"sub1"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"super-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"sub2"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"super-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"sub3"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"super-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|prepareSubmoduleConfigEntry
argument_list|(
name|config
argument_list|,
literal|"sub1"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|prepareSubmoduleConfigEntry
argument_list|(
name|config
argument_list|,
literal|"sub2"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|prepareSubmoduleConfigEntry
argument_list|(
name|config
argument_list|,
literal|"sub3"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|pushSubmoduleConfig
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|ObjectId
name|superPreviousId
init|=
name|pushChangeTo
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|)
decl_stmt|;
name|ObjectId
name|sub1Id
init|=
name|pushChangeTo
argument_list|(
name|sub1
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|ObjectId
name|sub2Id
init|=
name|pushChangeTo
argument_list|(
name|sub2
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|ObjectId
name|sub3Id
init|=
name|pushChangeTo
argument_list|(
name|sub3
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|approve
argument_list|(
name|getChangeId
argument_list|(
name|sub1
argument_list|,
name|sub1Id
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|approve
argument_list|(
name|getChangeId
argument_list|(
name|sub2
argument_list|,
name|sub2Id
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|approve
argument_list|(
name|getChangeId
argument_list|(
name|sub3
argument_list|,
name|sub3Id
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|getChangeId
argument_list|(
name|sub1
argument_list|,
name|sub1Id
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|expectToHaveSubmoduleState
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"sub1"
argument_list|,
name|sub1Id
argument_list|)
expr_stmt|;
name|expectToHaveSubmoduleState
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"sub2"
argument_list|,
name|sub2Id
argument_list|)
expr_stmt|;
name|expectToHaveSubmoduleState
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"sub3"
argument_list|,
name|sub3Id
argument_list|)
expr_stmt|;
name|superRepo
operator|.
name|git
argument_list|()
operator|.
name|fetch
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|call
argument_list|()
operator|.
name|getAdvertisedRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
name|assertWithMessage
argument_list|(
literal|"submodule subscription update "
operator|+
literal|"should have made one commit"
argument_list|)
operator|.
name|that
argument_list|(
name|superRepo
operator|.
name|getRepository
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"origin/master^"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|superPreviousId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRecursiveSubmodules ()
specifier|public
name|void
name|testRecursiveSubmodules
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|topRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"top-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|midRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"mid-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|bottomRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"bottom-project"
argument_list|)
decl_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"mid-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"top-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"bottom-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"mid-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|topRepo
argument_list|,
literal|"master"
argument_list|,
literal|"mid-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|midRepo
argument_list|,
literal|"master"
argument_list|,
literal|"bottom-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|ObjectId
name|bottomHead
init|=
name|pushChangeTo
argument_list|(
name|bottomRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|ObjectId
name|topHead
init|=
name|pushChangeTo
argument_list|(
name|topRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|bottomRepo
argument_list|,
name|bottomHead
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|topRepo
argument_list|,
name|topHead
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id2
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|midRepo
argument_list|,
literal|"master"
argument_list|,
literal|"bottom-project"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|topRepo
argument_list|,
literal|"master"
argument_list|,
literal|"mid-project"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTriangleSubmodules ()
specifier|public
name|void
name|testTriangleSubmodules
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|topRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"top-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|midRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"mid-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|bottomRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"bottom-project"
argument_list|)
decl_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"mid-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"top-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"bottom-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"mid-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"bottom-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"top-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|midRepo
argument_list|,
literal|"master"
argument_list|,
literal|"bottom-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|prepareSubmoduleConfigEntry
argument_list|(
name|config
argument_list|,
literal|"bottom-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|prepareSubmoduleConfigEntry
argument_list|(
name|config
argument_list|,
literal|"mid-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|pushSubmoduleConfig
argument_list|(
name|topRepo
argument_list|,
literal|"master"
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|ObjectId
name|bottomHead
init|=
name|pushChangeTo
argument_list|(
name|bottomRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|ObjectId
name|topHead
init|=
name|pushChangeTo
argument_list|(
name|topRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|String
name|id1
init|=
name|getChangeId
argument_list|(
name|bottomRepo
argument_list|,
name|bottomHead
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|getChangeId
argument_list|(
name|topRepo
argument_list|,
name|topHead
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id2
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id1
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|midRepo
argument_list|,
literal|"master"
argument_list|,
literal|"bottom-project"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|topRepo
argument_list|,
literal|"master"
argument_list|,
literal|"mid-project"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|topRepo
argument_list|,
literal|"master"
argument_list|,
literal|"bottom-project"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBranchCircularSubscription ()
specifier|public
name|void
name|testBranchCircularSubscription
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|topRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"top-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|midRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"mid-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|bottomRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"bottom-project"
argument_list|)
decl_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|midRepo
argument_list|,
literal|"master"
argument_list|,
literal|"bottom-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|topRepo
argument_list|,
literal|"master"
argument_list|,
literal|"mid-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|bottomRepo
argument_list|,
literal|"master"
argument_list|,
literal|"top-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"bottom-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"mid-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"mid-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"top-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"top-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"bottom-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|ObjectId
name|bottomMasterHead
init|=
name|pushChangeTo
argument_list|(
name|bottomRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|changeId
init|=
name|getChangeId
argument_list|(
name|bottomRepo
argument_list|,
name|bottomMasterHead
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|approve
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Branch level circular subscriptions detected"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"top-project,refs/heads/master"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"mid-project,refs/heads/master"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"bottom-project,refs/heads/master"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|midRepo
argument_list|,
literal|"master"
argument_list|,
literal|"bottom-project"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|topRepo
argument_list|,
literal|"master"
argument_list|,
literal|"mid-project"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProjectCircularSubscriptionWholeTopic ()
specifier|public
name|void
name|testProjectCircularSubscriptionWholeTopic
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|superRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"super-project"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|subRepo
init|=
name|createProjectWithPush
argument_list|(
literal|"subscribed-to-project"
argument_list|)
decl_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"subscribed-to-project"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"super-project"
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allowSubmoduleSubscription
argument_list|(
literal|"super-project"
argument_list|,
literal|"refs/heads/dev"
argument_list|,
literal|"subscribed-to-project"
argument_list|,
literal|"refs/heads/dev"
argument_list|)
expr_stmt|;
name|pushChangeTo
argument_list|(
name|subRepo
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|pushChangeTo
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|pushChangeTo
argument_list|(
name|subRepo
argument_list|,
literal|"dev"
argument_list|)
expr_stmt|;
name|pushChangeTo
argument_list|(
name|superRepo
argument_list|,
literal|"dev"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"subscribed-to-project"
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|createSubmoduleSubscription
argument_list|(
name|subRepo
argument_list|,
literal|"dev"
argument_list|,
literal|"super-project"
argument_list|,
literal|"dev"
argument_list|)
expr_stmt|;
name|ObjectId
name|subMasterHead
init|=
name|pushChangeTo
argument_list|(
name|subRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|ObjectId
name|superDevHead
init|=
name|pushChangeTo
argument_list|(
name|superRepo
argument_list|,
literal|"refs/for/dev"
argument_list|,
literal|"some message"
argument_list|,
literal|"same-topic"
argument_list|)
decl_stmt|;
name|approve
argument_list|(
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|subMasterHead
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|approve
argument_list|(
name|getChangeId
argument_list|(
name|superRepo
argument_list|,
name|superDevHead
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Project level circular subscriptions detected"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"subscribed-to-project"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"super-project"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|getChangeId
argument_list|(
name|subRepo
argument_list|,
name|subMasterHead
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|superRepo
argument_list|,
literal|"master"
argument_list|,
literal|"subscribed-to-project"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasSubmodule
argument_list|(
name|subRepo
argument_list|,
literal|"dev"
argument_list|,
literal|"super-project"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


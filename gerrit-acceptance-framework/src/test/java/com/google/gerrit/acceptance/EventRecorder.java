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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|FluentIterable
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
name|LinkedListMultimap
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
name|gerrit
operator|.
name|common
operator|.
name|UserScopedEventListener
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
name|DynamicSet
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
name|RegistrationHandle
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
name|CurrentUser
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
name|IdentifiedUser
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
name|data
operator|.
name|RefUpdateAttribute
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
name|events
operator|.
name|ChangeMergedEvent
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
name|events
operator|.
name|Event
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
name|events
operator|.
name|RefEvent
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
name|events
operator|.
name|RefUpdatedEvent
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
name|events
operator|.
name|ReviewerDeletedEvent
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
name|Singleton
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

begin_class
DECL|class|EventRecorder
specifier|public
class|class
name|EventRecorder
block|{
DECL|field|eventListenerRegistration
specifier|private
specifier|final
name|RegistrationHandle
name|eventListenerRegistration
decl_stmt|;
DECL|field|recordedEvents
specifier|private
specifier|final
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|RefEvent
argument_list|>
name|recordedEvents
decl_stmt|;
annotation|@
name|Singleton
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
block|{
DECL|field|eventListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|UserScopedEventListener
argument_list|>
name|eventListeners
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|Factory ( DynamicSet<UserScopedEventListener> eventListeners, IdentifiedUser.GenericFactory userFactory)
name|Factory
parameter_list|(
name|DynamicSet
argument_list|<
name|UserScopedEventListener
argument_list|>
name|eventListeners
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|)
block|{
name|this
operator|.
name|eventListeners
operator|=
name|eventListeners
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
block|}
DECL|method|create (TestAccount user)
specifier|public
name|EventRecorder
name|create
parameter_list|(
name|TestAccount
name|user
parameter_list|)
block|{
return|return
operator|new
name|EventRecorder
argument_list|(
name|eventListeners
argument_list|,
name|userFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|id
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|EventRecorder ( DynamicSet<UserScopedEventListener> eventListeners, final IdentifiedUser user)
specifier|public
name|EventRecorder
parameter_list|(
name|DynamicSet
argument_list|<
name|UserScopedEventListener
argument_list|>
name|eventListeners
parameter_list|,
specifier|final
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|recordedEvents
operator|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
expr_stmt|;
name|eventListenerRegistration
operator|=
name|eventListeners
operator|.
name|add
argument_list|(
operator|new
name|UserScopedEventListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onEvent
parameter_list|(
name|Event
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|ReviewerDeletedEvent
condition|)
block|{
name|recordedEvents
operator|.
name|put
argument_list|(
name|ReviewerDeletedEvent
operator|.
name|TYPE
argument_list|,
operator|(
name|ReviewerDeletedEvent
operator|)
name|e
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|instanceof
name|RefEvent
condition|)
block|{
name|RefEvent
name|event
init|=
operator|(
name|RefEvent
operator|)
name|e
decl_stmt|;
name|String
name|key
init|=
name|refEventKey
argument_list|(
name|event
operator|.
name|getType
argument_list|()
argument_list|,
name|event
operator|.
name|getProjectNameKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|event
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
name|recordedEvents
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|refEventKey (String type, String project, String ref)
specifier|private
specifier|static
name|String
name|refEventKey
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s-%s-%s"
argument_list|,
name|type
argument_list|,
name|project
argument_list|,
name|ref
argument_list|)
return|;
block|}
DECL|method|getRefUpdatedEvents ( String project, String refName, int expectedSize)
specifier|private
name|ImmutableList
argument_list|<
name|RefUpdatedEvent
argument_list|>
name|getRefUpdatedEvents
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|refName
parameter_list|,
name|int
name|expectedSize
parameter_list|)
block|{
name|String
name|key
init|=
name|refEventKey
argument_list|(
name|RefUpdatedEvent
operator|.
name|TYPE
argument_list|,
name|project
argument_list|,
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedSize
operator|==
literal|0
condition|)
block|{
name|assertThat
argument_list|(
name|recordedEvents
argument_list|)
operator|.
name|doesNotContainKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|assertThat
argument_list|(
name|recordedEvents
argument_list|)
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|RefUpdatedEvent
argument_list|>
name|events
init|=
name|FluentIterable
operator|.
name|from
argument_list|(
name|recordedEvents
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|RefUpdatedEvent
operator|.
name|class
operator|::
name|cast
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|events
argument_list|)
operator|.
name|hasSize
argument_list|(
name|expectedSize
argument_list|)
expr_stmt|;
return|return
name|events
return|;
block|}
DECL|method|getChangeMergedEvents ( String project, String branch, int expectedSize)
specifier|private
name|ImmutableList
argument_list|<
name|ChangeMergedEvent
argument_list|>
name|getChangeMergedEvents
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|int
name|expectedSize
parameter_list|)
block|{
name|String
name|key
init|=
name|refEventKey
argument_list|(
name|ChangeMergedEvent
operator|.
name|TYPE
argument_list|,
name|project
argument_list|,
name|branch
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedSize
operator|==
literal|0
condition|)
block|{
name|assertThat
argument_list|(
name|recordedEvents
argument_list|)
operator|.
name|doesNotContainKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|assertThat
argument_list|(
name|recordedEvents
argument_list|)
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|ChangeMergedEvent
argument_list|>
name|events
init|=
name|FluentIterable
operator|.
name|from
argument_list|(
name|recordedEvents
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|ChangeMergedEvent
operator|.
name|class
operator|::
name|cast
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|events
argument_list|)
operator|.
name|hasSize
argument_list|(
name|expectedSize
argument_list|)
expr_stmt|;
return|return
name|events
return|;
block|}
DECL|method|getReviewerDeletedEvents (int expectedSize)
specifier|private
name|ImmutableList
argument_list|<
name|ReviewerDeletedEvent
argument_list|>
name|getReviewerDeletedEvents
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
name|String
name|key
init|=
name|ReviewerDeletedEvent
operator|.
name|TYPE
decl_stmt|;
if|if
condition|(
name|expectedSize
operator|==
literal|0
condition|)
block|{
name|assertThat
argument_list|(
name|recordedEvents
argument_list|)
operator|.
name|doesNotContainKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|assertThat
argument_list|(
name|recordedEvents
argument_list|)
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|ReviewerDeletedEvent
argument_list|>
name|events
init|=
name|FluentIterable
operator|.
name|from
argument_list|(
name|recordedEvents
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|ReviewerDeletedEvent
operator|.
name|class
operator|::
name|cast
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|events
argument_list|)
operator|.
name|hasSize
argument_list|(
name|expectedSize
argument_list|)
expr_stmt|;
return|return
name|events
return|;
block|}
DECL|method|assertRefUpdatedEvents (String project, String branch, String... expected)
specifier|public
name|void
name|assertRefUpdatedEvents
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
modifier|...
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|RefUpdatedEvent
argument_list|>
name|events
init|=
name|getRefUpdatedEvents
argument_list|(
name|project
argument_list|,
name|branch
argument_list|,
name|expected
operator|.
name|length
operator|/
literal|2
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|RefUpdatedEvent
name|event
range|:
name|events
control|)
block|{
name|RefUpdateAttribute
name|actual
init|=
name|event
operator|.
name|refUpdate
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|oldRev
init|=
name|expected
index|[
name|i
index|]
operator|==
literal|null
condition|?
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|name
argument_list|()
else|:
name|expected
index|[
name|i
index|]
decl_stmt|;
name|String
name|newRev
init|=
name|expected
index|[
name|i
operator|+
literal|1
index|]
operator|==
literal|null
condition|?
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|name
argument_list|()
else|:
name|expected
index|[
name|i
operator|+
literal|1
index|]
decl_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|oldRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldRev
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|newRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|newRev
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|2
expr_stmt|;
block|}
block|}
DECL|method|assertRefUpdatedEvents (String project, String branch, RevCommit... expected)
specifier|public
name|void
name|assertRefUpdatedEvents
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|RevCommit
modifier|...
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|RefUpdatedEvent
argument_list|>
name|events
init|=
name|getRefUpdatedEvents
argument_list|(
name|project
argument_list|,
name|branch
argument_list|,
name|expected
operator|.
name|length
operator|/
literal|2
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|RefUpdatedEvent
name|event
range|:
name|events
control|)
block|{
name|RefUpdateAttribute
name|actual
init|=
name|event
operator|.
name|refUpdate
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|oldRev
init|=
name|expected
index|[
name|i
index|]
operator|==
literal|null
condition|?
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|name
argument_list|()
else|:
name|expected
index|[
name|i
index|]
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|newRev
init|=
name|expected
index|[
name|i
operator|+
literal|1
index|]
operator|==
literal|null
condition|?
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|name
argument_list|()
else|:
name|expected
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|name
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|oldRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldRev
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|newRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|newRev
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|2
expr_stmt|;
block|}
block|}
DECL|method|assertChangeMergedEvents (String project, String branch, String... expected)
specifier|public
name|void
name|assertChangeMergedEvents
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
modifier|...
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|ChangeMergedEvent
argument_list|>
name|events
init|=
name|getChangeMergedEvents
argument_list|(
name|project
argument_list|,
name|branch
argument_list|,
name|expected
operator|.
name|length
operator|/
literal|2
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ChangeMergedEvent
name|event
range|:
name|events
control|)
block|{
name|String
name|id
init|=
name|event
operator|.
name|change
operator|.
name|get
argument_list|()
operator|.
name|id
decl_stmt|;
name|assertThat
argument_list|(
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|event
operator|.
name|newRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|2
expr_stmt|;
block|}
block|}
DECL|method|assertReviewerDeletedEvents (String... expected)
specifier|public
name|void
name|assertReviewerDeletedEvents
parameter_list|(
name|String
modifier|...
name|expected
parameter_list|)
block|{
name|ImmutableList
argument_list|<
name|ReviewerDeletedEvent
argument_list|>
name|events
init|=
name|getReviewerDeletedEvents
argument_list|(
name|expected
operator|.
name|length
operator|/
literal|2
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ReviewerDeletedEvent
name|event
range|:
name|events
control|)
block|{
name|String
name|id
init|=
name|event
operator|.
name|change
operator|.
name|get
argument_list|()
operator|.
name|id
decl_stmt|;
name|assertThat
argument_list|(
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|String
name|reviewer
init|=
name|event
operator|.
name|reviewer
operator|.
name|get
argument_list|()
operator|.
name|email
decl_stmt|;
name|assertThat
argument_list|(
name|reviewer
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|2
expr_stmt|;
block|}
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|eventListenerRegistration
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


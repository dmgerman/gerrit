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
DECL|package|com.google.gerrit.server.notedb.rebuild
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
operator|.
name|rebuild
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|collect
operator|.
name|MultimapBuilder
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
name|SetMultimap
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
name|LinkedHashSet
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
name|PriorityQueue
import|;
end_import

begin_comment
comment|/**  * Helper to sort a list of events.  *  *<p>Events are sorted in two passes:  *  *<ol>  *<li>Sort by natural order (timestamp, patch set, author, etc.)  *<li>Postpone any events with dependencies to occur only after all of their dependencies, where  *       this violates natural order.  *</ol>  *  * {@link #sort()} modifies the event list in place (similar to {@link Collections#sort(List)}), but  * does not modify any event. In particular, events might end up out of order with respect to  * timestamp; callers are responsible for adjusting timestamps later if they prefer monotonicity.  */
end_comment

begin_class
DECL|class|EventSorter
class|class
name|EventSorter
block|{
DECL|field|out
specifier|private
specifier|final
name|List
argument_list|<
name|Event
argument_list|>
name|out
decl_stmt|;
DECL|field|sorted
specifier|private
specifier|final
name|LinkedHashSet
argument_list|<
name|Event
argument_list|>
name|sorted
decl_stmt|;
DECL|field|waiting
specifier|private
name|ListMultimap
argument_list|<
name|Event
argument_list|,
name|Event
argument_list|>
name|waiting
decl_stmt|;
DECL|field|deps
specifier|private
name|SetMultimap
argument_list|<
name|Event
argument_list|,
name|Event
argument_list|>
name|deps
decl_stmt|;
DECL|method|EventSorter (List<Event> events)
name|EventSorter
parameter_list|(
name|List
argument_list|<
name|Event
argument_list|>
name|events
parameter_list|)
block|{
name|LinkedHashSet
argument_list|<
name|Event
argument_list|>
name|all
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|events
argument_list|)
decl_stmt|;
name|out
operator|=
name|events
expr_stmt|;
for|for
control|(
name|Event
name|e
range|:
name|events
control|)
block|{
for|for
control|(
name|Event
name|d
range|:
name|e
operator|.
name|deps
control|)
block|{
name|checkArgument
argument_list|(
name|all
operator|.
name|contains
argument_list|(
name|d
argument_list|)
argument_list|,
literal|"dep %s of %s not in input list"
argument_list|,
name|d
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|all
operator|.
name|clear
argument_list|()
expr_stmt|;
name|sorted
operator|=
name|all
expr_stmt|;
comment|// Presized.
block|}
DECL|method|sort ()
name|void
name|sort
parameter_list|()
block|{
comment|// First pass: sort by natural order.
name|PriorityQueue
argument_list|<
name|Event
argument_list|>
name|todo
init|=
operator|new
name|PriorityQueue
argument_list|<>
argument_list|(
name|out
argument_list|)
decl_stmt|;
comment|// Populate waiting map after initial sort to preserve natural order.
name|waiting
operator|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|deps
operator|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|hashSetValues
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
for|for
control|(
name|Event
name|e
range|:
name|todo
control|)
block|{
for|for
control|(
name|Event
name|d
range|:
name|e
operator|.
name|deps
control|)
block|{
name|deps
operator|.
name|put
argument_list|(
name|e
argument_list|,
name|d
argument_list|)
expr_stmt|;
name|waiting
operator|.
name|put
argument_list|(
name|d
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Second pass: enforce dependencies.
name|int
name|size
init|=
name|out
operator|.
name|size
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|todo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|process
argument_list|(
name|todo
operator|.
name|remove
argument_list|()
argument_list|,
name|todo
argument_list|)
expr_stmt|;
block|}
name|checkState
argument_list|(
name|sorted
operator|.
name|size
argument_list|()
operator|==
name|size
argument_list|,
literal|"event sort expected %s elements, got %s"
argument_list|,
name|size
argument_list|,
name|sorted
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Modify out in-place a la Collections#sort.
name|out
operator|.
name|clear
argument_list|()
expr_stmt|;
name|out
operator|.
name|addAll
argument_list|(
name|sorted
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Event e, PriorityQueue<Event> todo)
name|void
name|process
parameter_list|(
name|Event
name|e
parameter_list|,
name|PriorityQueue
argument_list|<
name|Event
argument_list|>
name|todo
parameter_list|)
block|{
if|if
condition|(
name|sorted
operator|.
name|contains
argument_list|(
name|e
argument_list|)
condition|)
block|{
return|return;
comment|// Already emitted.
block|}
if|if
condition|(
operator|!
name|deps
operator|.
name|get
argument_list|(
name|e
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Not all events that e depends on have been emitted yet. Ignore e for
comment|// now; it will get added back to the queue in the block below once its
comment|// last dependency is processed.
return|return;
block|}
comment|// All events that e depends on have been emitted, so e can be emitted.
name|sorted
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// Remove e from the dependency set of all events waiting on e, and add
comment|// those events back to the queue in the original priority order for
comment|// reconsideration.
for|for
control|(
name|Event
name|w
range|:
name|waiting
operator|.
name|get
argument_list|(
name|e
argument_list|)
control|)
block|{
name|deps
operator|.
name|get
argument_list|(
name|w
argument_list|)
operator|.
name|remove
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|todo
operator|.
name|add
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


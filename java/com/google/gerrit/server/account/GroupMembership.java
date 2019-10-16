begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|entities
operator|.
name|AccountGroup
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Represents the set of groups that a single user is part of.  *  *<p>Different accounts systems (eg. LDAP, gerrit groups) provide concrete implementations.  */
end_comment

begin_interface
DECL|interface|GroupMembership
specifier|public
interface|interface
name|GroupMembership
block|{
DECL|field|EMPTY
name|GroupMembership
name|EMPTY
init|=
operator|new
name|ListGroupMembership
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * Returns {@code true} when the user this object was created for is a member of the specified    * group.    */
DECL|method|contains (AccountGroup.UUID groupId)
name|boolean
name|contains
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} when the user this object was created for is a member of any of the    * specified group.    */
DECL|method|containsAnyOf (Iterable<AccountGroup.UUID> groupIds)
name|boolean
name|containsAnyOf
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
parameter_list|)
function_decl|;
comment|/**    * Returns a set containing an input member of {@code contains(id)} is true.    *    *<p>This is batch form of contains that returns specific group information. Implementors may    * implement the method as:    *    *<pre>    * Set&lt;AccountGroup.UUID&gt; r = new HashSet&lt;&gt;();    * for (AccountGroup.UUID id : groupIds)    *   if (contains(id)) r.add(id);    *</pre>    */
DECL|method|intersection (Iterable<AccountGroup.UUID> groupIds)
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|intersection
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
parameter_list|)
function_decl|;
comment|/**    * Returns the set of groups that can be determined by the implementation. This may not return all    * groups the {@link #contains(AccountGroup.UUID)} would return {@code true} for, but will at    * least contain all top level groups. This restriction stems from the API of some group systems,    * which make it expensive to enumerate the members of a group.    */
DECL|method|getKnownGroups ()
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getKnownGroups
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


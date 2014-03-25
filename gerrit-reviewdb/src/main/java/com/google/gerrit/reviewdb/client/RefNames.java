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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_comment
comment|/** Constants and utilities for Gerrit-specific ref names. */
end_comment

begin_class
DECL|class|RefNames
specifier|public
class|class
name|RefNames
block|{
DECL|field|REFS_CHANGES
specifier|public
specifier|static
specifier|final
name|String
name|REFS_CHANGES
init|=
literal|"refs/changes/"
decl_stmt|;
comment|/** Note tree listing commits we refuse {@code refs/meta/reject-commits} */
DECL|field|REFS_REJECT_COMMITS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_REJECT_COMMITS
init|=
literal|"refs/meta/reject-commits"
decl_stmt|;
comment|/** Configuration settings for a project {@code refs/meta/config} */
DECL|field|REFS_CONFIG
specifier|public
specifier|static
specifier|final
name|String
name|REFS_CONFIG
init|=
literal|"refs/meta/config"
decl_stmt|;
comment|/** Preference settings for a user {@code refs/users} */
DECL|field|REFS_USER
specifier|public
specifier|static
specifier|final
name|String
name|REFS_USER
init|=
literal|"refs/users/"
decl_stmt|;
comment|/** Configurations of project-specific dashboards (canned search queries). */
DECL|field|REFS_DASHBOARDS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_DASHBOARDS
init|=
literal|"refs/meta/dashboards/"
decl_stmt|;
comment|/**    * Prefix applied to merge commit base nodes.    *<p>    * References in this directory should take the form    * {@code refs/cache-automerge/xx/yyyy...} where xx is    * the first two digits of the merge commit's object    * name, and yyyyy... is the remaining 38. The reference    * should point to a treeish that is the automatic merge    * result of the merge commit's parents.    */
DECL|field|REFS_CACHE_AUTOMERGE
specifier|public
specifier|static
specifier|final
name|String
name|REFS_CACHE_AUTOMERGE
init|=
literal|"refs/cache-automerge/"
decl_stmt|;
DECL|method|refsUsers (Account.Id accountId)
specifier|public
specifier|static
name|String
name|refsUsers
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|REFS_USER
argument_list|)
expr_stmt|;
name|int
name|account
init|=
name|accountId
operator|.
name|get
argument_list|()
decl_stmt|;
name|int
name|m
init|=
name|account
operator|%
literal|100
decl_stmt|;
if|if
condition|(
name|m
operator|<
literal|10
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|account
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|RefNames ()
specifier|private
name|RefNames
parameter_list|()
block|{   }
block|}
end_class

end_unit


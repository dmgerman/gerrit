begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|reviewdb
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
name|HashMap
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

begin_comment
comment|/** In-memory table of {@link GroupInfo}, indexed by {@link AccountGroup.Id}. */
end_comment

begin_class
DECL|class|GroupInfoCache
specifier|public
class|class
name|GroupInfoCache
block|{
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|GroupInfoCache
name|EMPTY
decl_stmt|;
static|static
block|{
name|EMPTY
operator|=
operator|new
name|GroupInfoCache
argument_list|()
expr_stmt|;
name|EMPTY
operator|.
name|groups
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
comment|/** Obtain an empty cache singleton. */
DECL|method|empty ()
specifier|public
specifier|static
name|GroupInfoCache
name|empty
parameter_list|()
block|{
return|return
name|EMPTY
return|;
block|}
DECL|field|groups
specifier|protected
name|Map
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|GroupInfo
argument_list|>
name|groups
decl_stmt|;
DECL|method|GroupInfoCache ()
specifier|protected
name|GroupInfoCache
parameter_list|()
block|{   }
DECL|method|GroupInfoCache (final Iterable<GroupInfo> list)
specifier|public
name|GroupInfoCache
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|GroupInfo
argument_list|>
name|list
parameter_list|)
block|{
name|groups
operator|=
operator|new
name|HashMap
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|GroupInfo
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|GroupInfo
name|gi
range|:
name|list
control|)
block|{
name|groups
operator|.
name|put
argument_list|(
name|gi
operator|.
name|getId
argument_list|()
argument_list|,
name|gi
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Lookup the group summary    *<p>    * The return value can take on one of three forms:    *<ul>    *<li><code>null</code>, if<code>id == null</code>.</li>    *<li>a valid info block, if<code>id</code> was loaded.</li>    *<li>an anonymous info block, if<code>id</code> was not loaded.</li>    *</ul>    *    * @param id the id desired.    * @return info block for the group.    */
DECL|method|get (final AccountGroup.Id id)
specifier|public
name|GroupInfo
name|get
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|GroupInfo
name|r
init|=
name|groups
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
operator|new
name|GroupInfo
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|groups
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
comment|/** Merge the information from another cache into this one. */
DECL|method|merge (final GroupInfoCache other)
specifier|public
name|void
name|merge
parameter_list|(
specifier|final
name|GroupInfoCache
name|other
parameter_list|)
block|{
assert|assert
name|this
operator|!=
name|EMPTY
assert|;
name|groups
operator|.
name|putAll
argument_list|(
name|other
operator|.
name|groups
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


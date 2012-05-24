begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|Cache
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
name|Sets
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
name|sshd
operator|.
name|SshCommand
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
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_class
DECL|class|CacheCommand
specifier|abstract
class|class
name|CacheCommand
extends|extends
name|SshCommand
block|{
annotation|@
name|Inject
DECL|field|cacheMap
specifier|protected
name|DynamicMap
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cacheMap
decl_stmt|;
DECL|method|cacheNames ()
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|cacheNames
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|names
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|plugin
range|:
name|cacheMap
operator|.
name|plugins
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|name
range|:
name|cacheMap
operator|.
name|byPlugin
argument_list|(
name|plugin
argument_list|)
operator|.
name|keySet
argument_list|()
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|cacheNameOf
argument_list|(
name|plugin
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|names
return|;
block|}
DECL|method|cacheNameOf (String plugin, String name)
specifier|protected
name|String
name|cacheNameOf
parameter_list|(
name|String
name|plugin
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"gerrit"
operator|.
name|equals
argument_list|(
name|plugin
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
else|else
block|{
return|return
name|plugin
operator|+
literal|"."
operator|+
name|name
return|;
block|}
block|}
block|}
end_class

end_unit


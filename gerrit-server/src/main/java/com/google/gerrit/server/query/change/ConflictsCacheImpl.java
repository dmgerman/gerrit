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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
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
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|CacheModule
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
name|Module
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ConflictsCacheImpl
specifier|public
class|class
name|ConflictsCacheImpl
implements|implements
name|ConflictsCache
block|{
DECL|field|NAME
specifier|public
specifier|final
specifier|static
name|String
name|NAME
init|=
literal|"conflicts"
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|persist
argument_list|(
name|NAME
argument_list|,
name|ConflictKey
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|37400
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ConflictsCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ConflictsCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|conflictsCache
specifier|private
specifier|final
name|Cache
argument_list|<
name|ConflictKey
argument_list|,
name|Boolean
argument_list|>
name|conflictsCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ConflictsCacheImpl ( @amedNAME) Cache<ConflictKey, Boolean> conflictsCache)
specifier|public
name|ConflictsCacheImpl
parameter_list|(
annotation|@
name|Named
argument_list|(
name|NAME
argument_list|)
name|Cache
argument_list|<
name|ConflictKey
argument_list|,
name|Boolean
argument_list|>
name|conflictsCache
parameter_list|)
block|{
name|this
operator|.
name|conflictsCache
operator|=
name|conflictsCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|put (ConflictKey key, Boolean value)
specifier|public
name|void
name|put
parameter_list|(
name|ConflictKey
name|key
parameter_list|,
name|Boolean
name|value
parameter_list|)
block|{
name|conflictsCache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getIfPresent (ConflictKey key)
specifier|public
name|Boolean
name|getIfPresent
parameter_list|(
name|ConflictKey
name|key
parameter_list|)
block|{
return|return
name|conflictsCache
operator|.
name|getIfPresent
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.git.receive
package|package
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
name|receive
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|BATCH_CHANGES_LIMIT
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
name|account
operator|.
name|AccountLimits
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
name|config
operator|.
name|GerritServerConfig
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
name|Config
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ReceiveConfig
class|class
name|ReceiveConfig
block|{
DECL|field|checkMagicRefs
specifier|final
name|boolean
name|checkMagicRefs
decl_stmt|;
DECL|field|checkReferencedObjectsAreReachable
specifier|final
name|boolean
name|checkReferencedObjectsAreReachable
decl_stmt|;
DECL|field|maxBatchCommits
specifier|final
name|int
name|maxBatchCommits
decl_stmt|;
DECL|field|disablePrivateChanges
specifier|final
name|boolean
name|disablePrivateChanges
decl_stmt|;
DECL|field|systemMaxBatchChanges
specifier|private
specifier|final
name|int
name|systemMaxBatchChanges
decl_stmt|;
DECL|field|limitsFactory
specifier|private
specifier|final
name|AccountLimits
operator|.
name|Factory
name|limitsFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReceiveConfig (@erritServerConfig Config config, AccountLimits.Factory limitsFactory)
name|ReceiveConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|AccountLimits
operator|.
name|Factory
name|limitsFactory
parameter_list|)
block|{
name|checkMagicRefs
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"checkMagicRefs"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|checkReferencedObjectsAreReachable
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"checkReferencedObjectsAreReachable"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|maxBatchCommits
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"maxBatchCommits"
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
name|systemMaxBatchChanges
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"receive"
argument_list|,
literal|"maxBatchChanges"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|disablePrivateChanges
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"disablePrivateChanges"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|limitsFactory
operator|=
name|limitsFactory
expr_stmt|;
block|}
DECL|method|getEffectiveMaxBatchChangesLimit (CurrentUser user)
specifier|public
name|int
name|getEffectiveMaxBatchChangesLimit
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
name|AccountLimits
name|limits
init|=
name|limitsFactory
operator|.
name|create
argument_list|(
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|limits
operator|.
name|hasExplicitRange
argument_list|(
name|BATCH_CHANGES_LIMIT
argument_list|)
condition|)
block|{
return|return
name|limits
operator|.
name|getRange
argument_list|(
name|BATCH_CHANGES_LIMIT
argument_list|)
operator|.
name|getMax
argument_list|()
return|;
block|}
return|return
name|systemMaxBatchChanges
return|;
block|}
block|}
end_class

end_unit


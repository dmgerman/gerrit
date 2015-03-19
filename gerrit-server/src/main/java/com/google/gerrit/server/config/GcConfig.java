begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

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
name|ConfigConstants
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GcConfig
specifier|public
class|class
name|GcConfig
block|{
DECL|field|scheduleConfig
specifier|private
specifier|final
name|ScheduleConfig
name|scheduleConfig
decl_stmt|;
DECL|field|aggressive
specifier|private
specifier|final
name|boolean
name|aggressive
decl_stmt|;
annotation|@
name|Inject
DECL|method|GcConfig (@erritServerConfig Config cfg)
name|GcConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|scheduleConfig
operator|=
operator|new
name|ScheduleConfig
argument_list|(
name|cfg
argument_list|,
name|ConfigConstants
operator|.
name|CONFIG_GC_SECTION
argument_list|)
expr_stmt|;
name|aggressive
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
name|ConfigConstants
operator|.
name|CONFIG_GC_SECTION
argument_list|,
literal|"aggressive"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|getScheduleConfig ()
specifier|public
name|ScheduleConfig
name|getScheduleConfig
parameter_list|()
block|{
return|return
name|scheduleConfig
return|;
block|}
DECL|method|isAggressive ()
specifier|public
name|boolean
name|isAggressive
parameter_list|()
block|{
return|return
name|aggressive
return|;
block|}
block|}
end_class

end_unit


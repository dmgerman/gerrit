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
DECL|package|com.google.gerrit.pgm.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|util
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ThreadSettingsConfig
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
name|schema
operator|.
name|DataSourceType
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
name|Injector
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
name|Key
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|// TODO(dborowitz): Not necessary once we switch to NoteDb.
end_comment

begin_comment
comment|/** Utility to limit threads used by a batch program. */
end_comment

begin_class
DECL|class|ThreadLimiter
specifier|public
class|class
name|ThreadLimiter
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ThreadLimiter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|limitThreads (Injector dbInjector, int threads)
specifier|public
specifier|static
name|int
name|limitThreads
parameter_list|(
name|Injector
name|dbInjector
parameter_list|,
name|int
name|threads
parameter_list|)
block|{
return|return
name|limitThreads
argument_list|(
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|Config
operator|.
name|class
argument_list|,
name|GerritServerConfig
operator|.
name|class
argument_list|)
argument_list|)
argument_list|,
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
argument_list|,
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|ThreadSettingsConfig
operator|.
name|class
argument_list|)
argument_list|,
name|threads
argument_list|)
return|;
block|}
DECL|method|limitThreads ( Config cfg, DataSourceType dst, ThreadSettingsConfig threadSettingsConfig, int threads)
specifier|private
specifier|static
name|int
name|limitThreads
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|DataSourceType
name|dst
parameter_list|,
name|ThreadSettingsConfig
name|threadSettingsConfig
parameter_list|,
name|int
name|threads
parameter_list|)
block|{
name|boolean
name|usePool
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"database"
argument_list|,
literal|"connectionpool"
argument_list|,
name|dst
operator|.
name|usePool
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|poolLimit
init|=
name|threadSettingsConfig
operator|.
name|getDatabasePoolLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|usePool
operator|&&
name|threads
operator|>
name|poolLimit
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Limiting program to "
operator|+
name|poolLimit
operator|+
literal|" threads due to database.poolLimit"
argument_list|)
expr_stmt|;
return|return
name|poolLimit
return|;
block|}
return|return
name|threads
return|;
block|}
DECL|method|ThreadLimiter ()
specifier|private
name|ThreadLimiter
parameter_list|()
block|{}
block|}
end_class

end_unit


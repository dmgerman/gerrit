begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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

begin_class
annotation|@
name|Singleton
DECL|class|ThreadSettingsConfig
specifier|public
class|class
name|ThreadSettingsConfig
block|{
DECL|field|sshdThreads
specifier|private
specifier|final
name|int
name|sshdThreads
decl_stmt|;
DECL|field|httpdMaxThreads
specifier|private
specifier|final
name|int
name|httpdMaxThreads
decl_stmt|;
DECL|field|sshdBatchThreads
specifier|private
specifier|final
name|int
name|sshdBatchThreads
decl_stmt|;
DECL|field|databasePoolLimit
specifier|private
specifier|final
name|int
name|databasePoolLimit
decl_stmt|;
annotation|@
name|Inject
DECL|method|ThreadSettingsConfig (@erritServerConfig Config cfg)
name|ThreadSettingsConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|int
name|cores
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
decl_stmt|;
name|sshdThreads
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"sshd"
argument_list|,
literal|"threads"
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|4
argument_list|,
literal|2
operator|*
name|cores
argument_list|)
argument_list|)
expr_stmt|;
name|httpdMaxThreads
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"httpd"
argument_list|,
literal|"maxThreads"
argument_list|,
literal|25
argument_list|)
expr_stmt|;
name|int
name|defaultDatabasePoolLimit
init|=
name|sshdThreads
operator|+
name|httpdMaxThreads
operator|+
literal|2
decl_stmt|;
name|databasePoolLimit
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"database"
argument_list|,
literal|"poolLimit"
argument_list|,
name|defaultDatabasePoolLimit
argument_list|)
expr_stmt|;
name|sshdBatchThreads
operator|=
name|cores
operator|==
literal|1
condition|?
literal|1
else|:
literal|2
expr_stmt|;
block|}
DECL|method|getDatabasePoolLimit ()
specifier|public
name|int
name|getDatabasePoolLimit
parameter_list|()
block|{
return|return
name|databasePoolLimit
return|;
block|}
DECL|method|getHttpdMaxThreads ()
specifier|public
name|int
name|getHttpdMaxThreads
parameter_list|()
block|{
return|return
name|httpdMaxThreads
return|;
block|}
DECL|method|getSshdThreads ()
specifier|public
name|int
name|getSshdThreads
parameter_list|()
block|{
return|return
name|sshdThreads
return|;
block|}
DECL|method|getSshdBatchTreads ()
specifier|public
name|int
name|getSshdBatchTreads
parameter_list|()
block|{
return|return
name|sshdBatchThreads
return|;
block|}
block|}
end_class

end_unit


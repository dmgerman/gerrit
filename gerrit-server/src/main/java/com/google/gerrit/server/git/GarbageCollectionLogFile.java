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
DECL|package|com.google.gerrit.server.git
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
name|extensions
operator|.
name|events
operator|.
name|LifecycleListener
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
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
name|util
operator|.
name|SystemLog
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
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|LogManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|PatternLayout
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
DECL|class|GarbageCollectionLogFile
specifier|public
class|class
name|GarbageCollectionLogFile
implements|implements
name|LifecycleListener
block|{
annotation|@
name|Inject
DECL|method|GarbageCollectionLogFile (SitePaths sitePaths, @GerritServerConfig Config config)
specifier|public
name|GarbageCollectionLogFile
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
if|if
condition|(
name|SystemLog
operator|.
name|shouldConfigure
argument_list|()
condition|)
block|{
name|initLogSystem
argument_list|(
name|sitePaths
operator|.
name|logs_dir
argument_list|,
name|config
operator|.
name|getBoolean
argument_list|(
literal|"log"
argument_list|,
literal|"rotate"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|LogManager
operator|.
name|getLogger
argument_list|(
name|GarbageCollection
operator|.
name|LOG_NAME
argument_list|)
operator|.
name|removeAllAppenders
argument_list|()
expr_stmt|;
block|}
DECL|method|initLogSystem (Path logdir, boolean rotate)
specifier|private
specifier|static
name|void
name|initLogSystem
parameter_list|(
name|Path
name|logdir
parameter_list|,
name|boolean
name|rotate
parameter_list|)
block|{
name|Logger
name|gcLogger
init|=
name|LogManager
operator|.
name|getLogger
argument_list|(
name|GarbageCollection
operator|.
name|LOG_NAME
argument_list|)
decl_stmt|;
name|gcLogger
operator|.
name|removeAllAppenders
argument_list|()
expr_stmt|;
name|gcLogger
operator|.
name|addAppender
argument_list|(
name|SystemLog
operator|.
name|createAppender
argument_list|(
name|logdir
argument_list|,
name|GarbageCollection
operator|.
name|LOG_NAME
argument_list|,
operator|new
name|PatternLayout
argument_list|(
literal|"[%d] %-5p %x: %m%n"
argument_list|)
argument_list|,
name|rotate
argument_list|)
argument_list|)
expr_stmt|;
name|gcLogger
operator|.
name|setAdditivity
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


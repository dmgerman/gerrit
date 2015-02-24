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
name|common
operator|.
name|FileUtil
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
name|git
operator|.
name|GarbageCollection
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
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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

begin_class
DECL|class|GarbageCollectionLogFile
specifier|public
class|class
name|GarbageCollectionLogFile
block|{
DECL|method|start (Path sitePath)
specifier|public
specifier|static
name|LifecycleListener
name|start
parameter_list|(
name|Path
name|sitePath
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|Path
name|logdir
init|=
name|FileUtil
operator|.
name|mkdirsOrDie
argument_list|(
operator|new
name|SitePaths
argument_list|(
name|sitePath
argument_list|)
operator|.
name|logs_dir
argument_list|,
literal|"Cannot create log directory"
argument_list|)
decl_stmt|;
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
name|logdir
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|LifecycleListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{       }
annotation|@
name|Override
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
block|}
return|;
block|}
DECL|method|initLogSystem (Path logdir)
specifier|private
specifier|static
name|void
name|initLogSystem
parameter_list|(
name|Path
name|logdir
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


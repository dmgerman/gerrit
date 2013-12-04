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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Formatter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogRecord
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|StreamHandler
import|;
end_import

begin_class
DECL|class|GuiceLogger
specifier|public
class|class
name|GuiceLogger
block|{
DECL|field|HANDLER
specifier|private
specifier|static
specifier|final
name|Handler
name|HANDLER
decl_stmt|;
static|static
block|{
name|HANDLER
operator|=
operator|new
name|StreamHandler
argument_list|(
name|System
operator|.
name|out
argument_list|,
operator|new
name|Formatter
argument_list|()
block|{
specifier|public
name|String
name|format
parameter_list|(
name|LogRecord
name|record
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"[Guice %s] %s%n"
argument_list|,
name|record
operator|.
name|getLevel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|record
operator|.
name|getMessage
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|HANDLER
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
DECL|method|GuiceLogger ()
specifier|private
name|GuiceLogger
parameter_list|()
block|{   }
DECL|method|getLogger ()
specifier|public
specifier|static
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"com.google.inject"
argument_list|)
return|;
block|}
DECL|method|enable ()
specifier|public
specifier|static
name|void
name|enable
parameter_list|()
block|{
name|Logger
name|guiceLogger
init|=
name|getLogger
argument_list|()
decl_stmt|;
name|guiceLogger
operator|.
name|addHandler
argument_list|(
name|GuiceLogger
operator|.
name|HANDLER
argument_list|)
expr_stmt|;
name|guiceLogger
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
DECL|method|disable ()
specifier|public
specifier|static
name|void
name|disable
parameter_list|()
block|{
name|Logger
name|guiceLogger
init|=
name|getLogger
argument_list|()
decl_stmt|;
name|guiceLogger
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|OFF
argument_list|)
expr_stmt|;
name|guiceLogger
operator|.
name|removeHandler
argument_list|(
name|GuiceLogger
operator|.
name|HANDLER
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


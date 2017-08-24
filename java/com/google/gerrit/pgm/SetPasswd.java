begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|ConsoleUI
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|Section
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|Section
operator|.
name|Factory
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

begin_class
DECL|class|SetPasswd
specifier|public
class|class
name|SetPasswd
block|{
DECL|field|ui
specifier|private
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|sections
specifier|private
name|Factory
name|sections
decl_stmt|;
annotation|@
name|Inject
DECL|method|SetPasswd (ConsoleUI ui, Section.Factory sections)
specifier|public
name|SetPasswd
parameter_list|(
name|ConsoleUI
name|ui
parameter_list|,
name|Section
operator|.
name|Factory
name|sections
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|sections
operator|=
name|sections
expr_stmt|;
block|}
DECL|method|run (String section, String key, String password)
specifier|public
name|void
name|run
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
block|{
name|Section
name|passwordSection
init|=
name|sections
operator|.
name|get
argument_list|(
name|section
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|ui
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|passwordSection
operator|.
name|setSecure
argument_list|(
name|key
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Set password for [%s]"
argument_list|,
name|section
argument_list|)
expr_stmt|;
name|passwordSection
operator|.
name|passwordForKey
argument_list|(
literal|"Enter password"
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


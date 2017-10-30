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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
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
name|client
operator|.
name|UiType
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
name|InitStep
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
name|Locale
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|InitExperimental
class|class
name|InitExperimental
implements|implements
name|InitStep
block|{
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|gerrit
specifier|private
specifier|final
name|Section
name|gerrit
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitExperimental (ConsoleUI ui, Section.Factory sections)
name|InitExperimental
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
name|gerrit
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Experimental features"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ui
operator|.
name|yesno
argument_list|(
literal|false
argument_list|,
literal|"Enable any experimental features"
argument_list|)
condition|)
block|{
return|return;
block|}
name|initUis
argument_list|()
expr_stmt|;
block|}
DECL|method|initUis ()
specifier|private
name|void
name|initUis
parameter_list|()
block|{
name|boolean
name|pg
init|=
name|ui
operator|.
name|yesno
argument_list|(
literal|true
argument_list|,
literal|"Default to PolyGerrit UI"
argument_list|)
decl_stmt|;
name|UiType
name|uiType
init|=
name|pg
condition|?
name|UiType
operator|.
name|POLYGERRIT
else|:
name|UiType
operator|.
name|GWT
decl_stmt|;
name|gerrit
operator|.
name|set
argument_list|(
literal|"ui"
argument_list|,
name|uiType
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|pg
condition|)
block|{
name|gerrit
operator|.
name|set
argument_list|(
literal|"enableGwtUi"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|ui
operator|.
name|yesno
argument_list|(
literal|true
argument_list|,
literal|"Enable GWT UI"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


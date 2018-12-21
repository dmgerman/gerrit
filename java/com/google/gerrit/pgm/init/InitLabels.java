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
name|LabelFunction
operator|.
name|MAX_WITH_BLOCK
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
name|AllProjectsConfig
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
DECL|class|InitLabels
specifier|public
class|class
name|InitLabels
implements|implements
name|InitStep
block|{
DECL|field|KEY_COPY_ALL_SCORES_IF_NO_CODE_CHANGE
specifier|private
specifier|static
specifier|final
name|String
name|KEY_COPY_ALL_SCORES_IF_NO_CODE_CHANGE
init|=
literal|"copyAllScoresIfNoCodeChange"
decl_stmt|;
DECL|field|KEY_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|KEY_LABEL
init|=
literal|"label"
decl_stmt|;
DECL|field|KEY_FUNCTION
specifier|private
specifier|static
specifier|final
name|String
name|KEY_FUNCTION
init|=
literal|"function"
decl_stmt|;
DECL|field|KEY_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|KEY_VALUE
init|=
literal|"value"
decl_stmt|;
DECL|field|LABEL_VERIFIED
specifier|private
specifier|static
specifier|final
name|String
name|LABEL_VERIFIED
init|=
literal|"Verified"
decl_stmt|;
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|allProjectsConfig
specifier|private
specifier|final
name|AllProjectsConfig
name|allProjectsConfig
decl_stmt|;
DECL|field|installVerified
specifier|private
name|boolean
name|installVerified
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitLabels (ConsoleUI ui, AllProjectsConfig allProjectsConfig)
name|InitLabels
parameter_list|(
name|ConsoleUI
name|ui
parameter_list|,
name|AllProjectsConfig
name|allProjectsConfig
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
name|allProjectsConfig
operator|=
name|allProjectsConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|Config
name|cfg
init|=
name|allProjectsConfig
operator|.
name|load
argument_list|()
operator|.
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|cfg
operator|==
literal|null
operator|||
operator|!
name|cfg
operator|.
name|getSubsections
argument_list|(
name|KEY_LABEL
argument_list|)
operator|.
name|contains
argument_list|(
name|LABEL_VERIFIED
argument_list|)
condition|)
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Review Labels"
argument_list|)
expr_stmt|;
name|installVerified
operator|=
name|ui
operator|.
name|yesno
argument_list|(
literal|false
argument_list|,
literal|"Install Verified label"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|postRun ()
specifier|public
name|void
name|postRun
parameter_list|()
throws|throws
name|Exception
block|{
name|Config
name|cfg
init|=
name|allProjectsConfig
operator|.
name|load
argument_list|()
operator|.
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|installVerified
condition|)
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|KEY_LABEL
argument_list|,
name|LABEL_VERIFIED
argument_list|,
name|KEY_FUNCTION
argument_list|,
name|MAX_WITH_BLOCK
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setStringList
argument_list|(
name|KEY_LABEL
argument_list|,
name|LABEL_VERIFIED
argument_list|,
name|KEY_VALUE
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"-1 Fails"
argument_list|,
literal|"0 No score"
argument_list|,
literal|"+1 Verified"
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
name|KEY_LABEL
argument_list|,
name|LABEL_VERIFIED
argument_list|,
name|KEY_COPY_ALL_SCORES_IF_NO_CODE_CHANGE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProjectsConfig
operator|.
name|save
argument_list|(
literal|"Configure 'Verified' label"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


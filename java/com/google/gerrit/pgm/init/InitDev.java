begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitFlags
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|InitDev
specifier|public
class|class
name|InitDev
implements|implements
name|InitStep
block|{
DECL|field|flags
specifier|private
specifier|final
name|InitFlags
name|flags
decl_stmt|;
DECL|field|plugins
specifier|private
specifier|final
name|Section
name|plugins
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitDev (InitFlags flags, Section.Factory sections)
name|InitDev
parameter_list|(
name|InitFlags
name|flags
parameter_list|,
name|Section
operator|.
name|Factory
name|sections
parameter_list|)
block|{
name|this
operator|.
name|flags
operator|=
name|flags
expr_stmt|;
name|this
operator|.
name|plugins
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"plugins"
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
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|flags
operator|.
name|dev
condition|)
block|{
return|return;
block|}
name|plugins
operator|.
name|set
argument_list|(
literal|"allowRemoteAdmin"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


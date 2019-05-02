begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CharMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_class
DECL|class|GerritTestName
specifier|public
class|class
name|GerritTestName
implements|implements
name|TestRule
block|{
DECL|field|delegate
specifier|private
specifier|final
name|TestName
name|delegate
init|=
operator|new
name|TestName
argument_list|()
decl_stmt|;
DECL|method|getSanitizedMethodName ()
specifier|public
name|String
name|getSanitizedMethodName
parameter_list|()
block|{
name|String
name|name
init|=
name|delegate
operator|.
name|getMethodName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|name
operator|=
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'a'
argument_list|,
literal|'z'
argument_list|)
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'A'
argument_list|,
literal|'Z'
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'0'
argument_list|,
literal|'9'
argument_list|)
argument_list|)
operator|.
name|negate
argument_list|()
operator|.
name|replaceFrom
argument_list|(
name|name
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
name|name
operator|=
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'_'
argument_list|)
operator|.
name|trimTrailingFrom
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|apply (Statement base, Description description)
specifier|public
name|Statement
name|apply
parameter_list|(
name|Statement
name|base
parameter_list|,
name|Description
name|description
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|apply
argument_list|(
name|base
argument_list|,
name|description
argument_list|)
return|;
block|}
block|}
end_class

end_unit


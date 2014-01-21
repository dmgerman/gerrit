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
DECL|package|com.google.gerrit.acceptance.rest.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|account
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|RestResponse
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountDiffPreference
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
name|account
operator|.
name|GetDiffPreferences
operator|.
name|DiffPreferencesInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|GetDiffPreferencesIT
specifier|public
class|class
name|GetDiffPreferencesIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|getDiffPreferencesOfNonExistingAccount_NotFound ()
specifier|public
name|void
name|getDiffPreferencesOfNonExistingAccount_NotFound
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|,
name|adminSession
operator|.
name|get
argument_list|(
literal|"/accounts/non-existing/preferences.diff"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getDiffPreferences ()
specifier|public
name|void
name|getDiffPreferences
parameter_list|()
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/accounts/"
operator|+
name|admin
operator|.
name|email
operator|+
literal|"/preferences.diff"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|DiffPreferencesInfo
name|diffPreferences
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|DiffPreferencesInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertDiffPreferences
argument_list|(
operator|new
name|AccountDiffPreference
argument_list|(
name|admin
operator|.
name|id
argument_list|)
argument_list|,
name|diffPreferences
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDiffPreferences (AccountDiffPreference expected, DiffPreferencesInfo actual)
specifier|private
specifier|static
name|void
name|assertDiffPreferences
parameter_list|(
name|AccountDiffPreference
name|expected
parameter_list|,
name|DiffPreferencesInfo
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|getContext
argument_list|()
argument_list|,
name|actual
operator|.
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isExpandAllComments
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|expandAllComments
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|getIgnoreWhitespace
argument_list|()
argument_list|,
name|actual
operator|.
name|ignoreWhitespace
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isIntralineDifference
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|intralineDifference
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|getLineLength
argument_list|()
argument_list|,
name|actual
operator|.
name|lineLength
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isManualReview
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|manualReview
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isRetainHeader
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|retainHeader
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isShowLineEndings
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|showLineEndings
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isShowTabs
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|showTabs
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isShowWhitespaceErrors
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|showWhitespaceErrors
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isSkipDeleted
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|skipDeleted
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isSkipUncommented
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|skipUncommented
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|isSyntaxHighlighting
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|syntaxHighlighting
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|getTabSize
argument_list|()
argument_list|,
name|actual
operator|.
name|tabSize
argument_list|)
expr_stmt|;
block|}
DECL|method|toBoolean (Boolean b)
specifier|private
specifier|static
name|boolean
name|toBoolean
parameter_list|(
name|Boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|b
operator|.
name|booleanValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit


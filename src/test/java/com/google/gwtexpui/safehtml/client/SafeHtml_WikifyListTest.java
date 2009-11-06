begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
comment|// distributed under the License is distributed on an "<p>AS IS" BASIS,
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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|SafeHtml_WikifyListTest
specifier|public
class|class
name|SafeHtml_WikifyListTest
extends|extends
name|TestCase
block|{
DECL|field|BEGIN_LIST
specifier|private
specifier|static
specifier|final
name|String
name|BEGIN_LIST
init|=
literal|"<ul class=\"gwtexpui-SafeHtml-WikiList\">"
decl_stmt|;
DECL|field|END_LIST
specifier|private
specifier|static
specifier|final
name|String
name|END_LIST
init|=
literal|"</ul>"
decl_stmt|;
DECL|method|item (String raw)
specifier|private
specifier|static
name|String
name|item
parameter_list|(
name|String
name|raw
parameter_list|)
block|{
return|return
literal|"<li>"
operator|+
name|raw
operator|+
literal|"</li>"
return|;
block|}
DECL|method|testBulletList1 ()
specifier|public
name|void
name|testBulletList1
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\n\n* line 1\n* 2nd line"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>A</p>"
comment|//
operator|+
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"line 1"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"2nd line"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBulletList2 ()
specifier|public
name|void
name|testBulletList2
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\n\n* line 1\n* 2nd line\n\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>A</p>"
comment|//
operator|+
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"line 1"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"2nd line"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
operator|+
literal|"<p>B</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBulletList3 ()
specifier|public
name|void
name|testBulletList3
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"* line 1\n* 2nd line\n\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"line 1"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"2nd line"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
operator|+
literal|"<p>B</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBulletList4 ()
specifier|public
name|void
name|testBulletList4
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"To see this bug, you have to:\n"
comment|//
operator|+
literal|"* Be on IMAP or EAS (not on POP)\n"
comment|//
operator|+
literal|"* Be very unlucky\n"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>To see this bug, you have to:</p>"
comment|//
operator|+
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"Be on IMAP or EAS (not on POP)"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"Be very unlucky"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBulletList5 ()
specifier|public
name|void
name|testBulletList5
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"To see this bug,\n"
comment|//
operator|+
literal|"you have to:\n"
comment|//
operator|+
literal|"* Be on IMAP or EAS (not on POP)\n"
comment|//
operator|+
literal|"* Be very unlucky\n"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>To see this bug, you have to:</p>"
comment|//
operator|+
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"Be on IMAP or EAS (not on POP)"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"Be very unlucky"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDashList1 ()
specifier|public
name|void
name|testDashList1
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\n\n- line 1\n- 2nd line"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>A</p>"
comment|//
operator|+
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"line 1"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"2nd line"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDashList2 ()
specifier|public
name|void
name|testDashList2
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\n\n- line 1\n- 2nd line\n\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>A</p>"
comment|//
operator|+
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"line 1"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"2nd line"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
operator|+
literal|"<p>B</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDashList3 ()
specifier|public
name|void
name|testDashList3
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"- line 1\n- 2nd line\n\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BEGIN_LIST
comment|//
operator|+
name|item
argument_list|(
literal|"line 1"
argument_list|)
comment|//
operator|+
name|item
argument_list|(
literal|"2nd line"
argument_list|)
comment|//
operator|+
name|END_LIST
comment|//
operator|+
literal|"<p>B</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|html (String text)
specifier|private
specifier|static
name|SafeHtml
name|html
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|text
argument_list|)
operator|.
name|toSafeHtml
argument_list|()
return|;
block|}
block|}
end_class

end_unit

